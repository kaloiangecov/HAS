package has.Utils;

import has.Employee.Employee;
import has.Employee.EmployeeDTO;
import has.Employee.EmployeeService;
import has.Request.Request;
import has.Task.Task;
import has.Task.TaskRepository;
import has.Task.TaskService;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by kaloi on 3/13/2017.
 */
@Component
public class TaskHandler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeService employeeService;

    private static final int TASK_STATUS_CREATED = 1;
    private static final String START_MORNING_SHIFT = "06:00";
    private static final String END_MORNING_SHIFT = "14:00";
    private static final String START_LUNCH_SHIFT = "14:00";
    private static final String END_LUNCH_SHIFT = "22:00";
    private static final String START_NIGHT_SHIFT = "22:00";
    private static final String END_NIGHT_SHIFT = "06:00";
    private static final int MORNING_SHIFT = 0;
    private static final int LUNCH_SHIFT = 1;
    private static final int NIGHT_SHIFT = 2;

    private Map<Integer, Shift> shifts = new HashMap<>();

    {
        Shift shift1 = new Shift(START_MORNING_SHIFT, END_MORNING_SHIFT);
        Shift shift2 = new Shift(START_LUNCH_SHIFT, END_LUNCH_SHIFT);
        Shift shift3 = new Shift(START_NIGHT_SHIFT, END_NIGHT_SHIFT);
        shifts.put(MORNING_SHIFT, shift1);
        shifts.put(LUNCH_SHIFT, shift2);
        shifts.put(NIGHT_SHIFT, shift3);
    }

    public Task createTaskFromRequest(Request request) {
        Task task = new Task();
        task.setTitle("Request " + request.getId());
        task.setDescription(createDescription(request));
        task.setRequest(request);
        task.setAssigner("SYSTEM");
        task.setTargetTime(request.getTargetTime());
        task.setTimePlaced(request.getTimePlaced());
        task.setPriority(2);
        task = assignTask(task);
        //TODO set description, employee, duration and target time(евентуално)
        return taskRepository.save(task);
    }

    public Task assignTask(Task task) {

        if (task.getTargetTime() == null) {


            List<EmployeeDTO> employeesOnShift
                    = employeeService.getEmployeesOnShift(findShift(new LocalTime()));
            if (employeesOnShift != null) {
                LocalTime timeNow = new LocalTime();
                LocalTime timeToAdd = LocalTime.parse(task.getDuration());
                timeNow.plusMinutes(timeToAdd.getMinuteOfHour());
                timeNow.plusHours(timeToAdd.getHourOfDay());
            }


        } else {

            List<EmployeeDTO> employeesOnShift
                    = employeeService.getEmployeesOnShift(findShift(LocalTime.parse(task.getTargetTime())));

            System.out.println("shift: " + findShift(LocalTime.parse(task.getTargetTime())));
            System.out.println("employees on shift: " + employeesOnShift.size());

            if (completeTaskBeforeShiftEnd(task)) {
                List<EmployeeDTO> availableEmployees = availableForTask(employeesOnShift, task);
                System.out.println("can take task: " + availableEmployees.size());
                if (!availableEmployees.isEmpty()) {
                    task = assignToLeastBusy(task, availableEmployees);
                }
            }

        }

        return task;
    }

    private Task assignToLeastBusy(Task task, List<EmployeeDTO> availableEmployees) {
        EmployeeDTO leastBusy = null;

        for (EmployeeDTO employeeDTO : availableEmployees) {
            for (Task employeeTask : employeeDTO.getTasks()) {
                employeeDTO.setSumDuration(
                        addTime(LocalTime.parse(employeeDTO.getSumDuration()),
                                LocalTime.parse(employeeTask.getDuration())).toString());
            }
            if (leastBusy == null) {
                leastBusy = employeeDTO;
            } else if (!firstDurationLessThanSecond(LocalTime.parse(leastBusy.getSumDuration()), LocalTime.parse(employeeDTO.getSumDuration()))) {
                leastBusy = employeeDTO;
            }
        }
        System.out.println(leastBusy.getWorkingSchedule().getEmployee().getPersonalData().getFullName());
        task.setAssignee(leastBusy.getWorkingSchedule().getEmployee());
        return task;
    }

    public String createDescription(Request req) {
        StringBuilder description = new StringBuilder();
        description
                .append("Room request: " + req.getId() + System.lineSeparator())
                .append("Type of request: " + req.getType())
                .append("From room: " + req.getReservationGuest().getReservation().getRoom());
        if (req.getMealRequests() != null) {
            description.append("Meals requested: " + req.getMealRequests());
        }
        return description.toString();
    }

    private Employee getLeastDurationEmployee(List<Employee> employees) {
        //TODO not implemented yet
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(employees.size());
        return employees.get(index);
    }

    private int findShift(LocalTime timeNow) {

        LocalTime morningShiftStart = LocalTime.parse(START_MORNING_SHIFT);
        LocalTime morningShiftEnd = LocalTime.parse(END_MORNING_SHIFT);
        LocalTime lunchShiftStart = LocalTime.parse(START_LUNCH_SHIFT);
        LocalTime lunchShiftEnd = LocalTime.parse(END_LUNCH_SHIFT);
        LocalTime nightShiftStart = LocalTime.parse(START_NIGHT_SHIFT);
        LocalTime nightShiftEnd = LocalTime.parse(END_NIGHT_SHIFT);

        if (timeNow.isAfter(morningShiftStart) && timeNow.isBefore(morningShiftEnd)) {
            return MORNING_SHIFT;
        } else if (timeNow.isAfter(lunchShiftStart) && timeNow.isBefore(lunchShiftEnd)) {
            return LUNCH_SHIFT;
        } else if (timeNow.isAfter(nightShiftStart) || timeNow.isBefore(nightShiftEnd)) {
            return NIGHT_SHIFT;
        }
        return 255;
    }

    private int getHours(String time) {
        return LocalTime.parse(time).getHourOfDay();
    }

    private int getMinutes(String time) {
        return LocalTime.parse(time).getMinuteOfHour();
    }

    private LocalTime addTime(LocalTime time1, LocalTime time2) {
        time1 = time1.plusMinutes(time2.getMinuteOfHour());
        time1 = time1.plusHours(time2.getHourOfDay());
        return time1;
    }

    private boolean completeTaskBeforeShiftEnd(Task task) {
        LocalTime targetTime = LocalTime.parse(task.getTargetTime());
        LocalTime endTime = LocalTime.parse(task.getDuration());
        endTime = addTime(targetTime, endTime);
        if (endTime.isBefore(LocalTime.parse(shifts.get(findShift(targetTime)).getEndShift()))) {
            return true;
        }
        return false;
    }

    private List<EmployeeDTO> availableForTask(List<EmployeeDTO> employees, Task task) {
        List<EmployeeDTO> availableEmployees = new ArrayList<>();

        for (EmployeeDTO employeeDTO : employees) {
            boolean notLegit = false;
            for (Task employeeTask : employeeDTO.getTasks()) {
                if (employeeTask.getTargetTime() != null) {
                    if (!notIntersecting(task, employeeTask)) {
                        notLegit = true;
                    }
                }
            }
            if (notLegit == false) {
                availableEmployees.add(employeeDTO);
            }
        }
        return availableEmployees;
    }

    private boolean notIntersecting(Task task1, Task task2) {
        LocalTime task1TimeStart = LocalTime.parse(task1.getTargetTime());
        LocalTime task1TimeEnd = addTime(LocalTime.parse(task1.getTargetTime()),
                LocalTime.parse(task1.getDuration()));
        LocalTime task2TimeStart = LocalTime.parse(task2.getTargetTime());
        LocalTime task2TimeEnd = addTime(LocalTime.parse(task2.getTargetTime()),
                LocalTime.parse(task2.getDuration()));

        if (task1TimeStart.isAfter(task2TimeEnd) || task1TimeEnd.isBefore(task2TimeStart)) {
            return true;
        }
        return false;
    }

    private Task manualHandleTask(Task task) {
        return task;
    }

    private boolean firstDurationLessThanSecond(LocalTime lastEmployeeTime, LocalTime currentEmployeeTime) {

        if (lastEmployeeTime.isBefore(currentEmployeeTime)) {
            return true;
        }
        if (lastEmployeeTime.equals(currentEmployeeTime)) {
            return false;
        }
        return false;
    }

    private void organiseTasks(Employee employee) {

    }
}

package has.Utils;

import has.Employee.EmployeeDTO;
import has.Employee.EmployeeRepository;
import has.Employee.EmployeeService;
import has.Request.Request;
import has.Task.Task;
import has.Task.TaskRepository;
import has.Task.TaskService;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
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

    @Autowired
    private EmployeeRepository employeeRepository;

    private TimeFormatter timeFormatter;

    private static final int TASK_STATUS_SCHEDULED = 0;

    private static final String FIVE_MINUTES = "00:05";
    private static final String START_MORNING_SHIFT = "06:00";
    private static final String END_MORNING_SHIFT = "14:00";
    private static final String START_LUNCH_SHIFT = "14:00";
    private static final String END_LUNCH_SHIFT = "22:00";
    private static final String START_NIGHT_SHIFT = "22:00";
    private static final String END_NIGHT_SHIFT = "06:00";
    private static final int MORNING_SHIFT = 0;
    private static final int LUNCH_SHIFT = 1;
    private static final int NIGHT_SHIFT = 2;

    private static final int HIGH_PRIORITY = 0;
    private static final int MEDIUM_PRIORITY = 1;
    private static final int LOW_PRIORITY = 2;

    private static final int FIRST = 0;
    private static final int ONE_ELEMENT = 1;

    private static final int NOT_INITIALIZED = 255;

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
        task.setTargetTime(request.getTargetTime());
        task.setTimePlaced(new Date().toString());
        task.setPriority(2);
        task = assignTask(task, findShift(new LocalTime()));
        //TODO set description, employee, duration and target time(евентуално)
        return taskRepository.save(task);
    }

    public Task assignTask(Task task, int shift) {

        task.setAssigner("SYSTEM");
        if (task.getTargetTime() == null) {
            List<EmployeeDTO> employeesOnShift;
            if (shift == NOT_INITIALIZED) {
                employeesOnShift = employeeService.getEmployeesOnShift(findShift(new LocalTime()));
            } else {
                employeesOnShift = employeeService.getEmployeesOnShift(shift);
            }
            if (!employeesOnShift.isEmpty()) {
                if (task.getPriority() < LOW_PRIORITY) {
                    task = assignAccordingToPriority(task, employeesOnShift);
                } else {
                    task = assignToLeastBusy(task, employeesOnShift);
                }
            } else {
                task = resolveConflict(task);
            }
        } else {
            if (completeTaskBeforeShiftEnd(task)) {
                List<EmployeeDTO> employeesOnShift = findEmployeesOnShiftDTO(parse(task.getTargetTime()));
                List<EmployeeDTO> availableEmployees = availableForTaskTargetTime(employeesOnShift, task);
                if (!availableEmployees.isEmpty()) {
                    task = assignToLeastBusy(task, availableEmployees);
                } else {
                    task = resolveConflict(task);
                }
            } else {
                task = resolveConflict(task);
            }
        }

        return task;
    }

    public List<EmployeeDTO> findEmployeesOnShiftDTO(LocalTime localTime) {
        return employeeService.getEmployeesOnShift(findShift(localTime));

    }

    private Task assignToLeastBusy(Task task, List<EmployeeDTO> availableEmployees) {
        EmployeeDTO leastBusy = null;

        for (EmployeeDTO employeeDTO : availableEmployees) {
            if (leastBusy == null) {
                leastBusy = employeeDTO;
            } else if (!firstDurationLessThanSecond(parse(leastBusy.getSumDuration()), parse(employeeDTO.getSumDuration()))) {
                leastBusy = employeeDTO;
            }
        }
        if (availableEmployees.size() == ONE_ELEMENT) {
            leastBusy = availableEmployees.get(FIRST);
        }
        task.setAssignee(leastBusy.getWorkingSchedule().getEmployee());
        leastBusy.updateTask(task);
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

    public int findShift(LocalTime timeNow) {

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
        } else if (timeNow.equals(morningShiftStart)) {
            return MORNING_SHIFT;
        } else if (timeNow.equals(lunchShiftStart)) {
            return LUNCH_SHIFT;
        } else if (timeNow.equals(nightShiftStart)) {
            return NIGHT_SHIFT;
        }
        return 255;
    }

    private LocalTime addTime(LocalTime time1, LocalTime time2) {
        time1 = time1.plusMinutes(time2.getMinuteOfHour());
        time1 = time1.plusHours(time2.getHourOfDay());
        return time1;
    }

    private boolean completeTaskBeforeShiftEnd(Task task) {
        if (task.getTargetTime() != null) {
            task.setStartTime(task.getTargetTime());
        }
        LocalTime targetTime = parse(task.getStartTime());
        LocalTime endTime = parse(task.getDuration());
        endTime = addTime(targetTime, endTime);
        String shiftEndTime = shifts.get(findShift(targetTime)).getEndShift();
        if (shiftEndTime.equals(END_NIGHT_SHIFT) && endTime.isAfter(parse(shiftEndTime))) {
            return true;
        } else if (endTime.isBefore(parse(shiftEndTime))) {
            return true;
        }
        return false;
    }

    private List<EmployeeDTO> availableForTaskTargetTime(List<EmployeeDTO> employees, Task task) {
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
        LocalTime task1TimeStart = parse(task1.getStartTime());
        LocalTime task1TimeEnd = addTime(parse(task1.getStartTime()),
                parse(task1.getDuration()));
        LocalTime task2TimeStart = parse(task2.getTargetTime());
        LocalTime task2TimeEnd = addTime(parse(task2.getStartTime()),
                parse(task2.getDuration()));

        if (task1TimeStart.isAfter(task2TimeEnd) || task1TimeEnd.isBefore(task2TimeStart)) {
            return true;
        }
        return false;
    }

    private Task resolveConflict(Task task) {
        Task resolveConflict = new Task();
        taskRepository.save(task);
        //TODO: tasks should have unique identifier other than the DB ID;
        resolveConflict.setTitle("Task conflict");
        resolveConflict.setDuration("00:10");
        resolveConflict.setDescription("There is conflict involving task with id: " + task.getId());
        resolveConflict.setPriority(HIGH_PRIORITY);
        resolveConflict.setAssigner("SYSTEM");
        resolveConflict.setStartTime(new LocalTime().toString());
        resolveConflict.setStatus(TASK_STATUS_SCHEDULED);

        //TODO: task should be sent to a MANAGER
        resolveConflict.setAssignee(employeeRepository.findOne(1L));
        return resolveConflict;
    }

    private boolean firstDurationLessThanSecond(LocalTime lastEmployeeTime, LocalTime currentEmployeeTime) {
        if (lastEmployeeTime.isBefore(currentEmployeeTime)) {
            return true;
        }
        if (lastEmployeeTime.equals(currentEmployeeTime)) {
            return true;
        }
        return false;
    }

    public List<Task> organiseTasks(EmployeeDTO employeeDTO) {
        Task lastTask = null;
        List<Task> targetTimeTasks = targetTimeTasks = employeeDTO.getTargetTimeTasks();
        List<Task> tasks = employeeDTO.getTasks();
        Task currentTask = employeeDTO.getCurrentTask();
        tasks = bubbleSortByPriority(tasks);
        List<Task> nextShiftTasks = new ArrayList<>();

        for (Task task : tasks) {
            if (task.getStatus() == TASK_STATUS_SCHEDULED) {
                if (lastTask != null) {
                    if (task.getTargetTime() == null) {
                        task.setStartTime(addTime(parse(lastTask.getFinishTime()), parse(FIVE_MINUTES)).toString());
                        task.setFinishTime(addTime(parse(task.getStartTime()), parse(task.getDuration())).toString());

                        Task intersectingTask = findIntersectingTask(targetTimeTasks, task);
                        if (intersectingTask != null) {
                            if (!completeTaskBeforeShiftEnd(task)) {
                                nextShiftTasks.add(task);
                            }
                        } else {
                            task.setStartTime(addTime(parse(lastTask.getFinishTime()), parse(FIVE_MINUTES)).toString());
                            task.setFinishTime(addTime(parse(task.getStartTime()), parse(task.getDuration())).toString());
                            if (!completeTaskBeforeShiftEnd(task)) {
                                nextShiftTasks.add(task);
                            }
                        }
                    }
                } else {
                    if (currentTask == null) {
                        task.setStartTime(timeFormatter.hoursAndMinutes(new LocalTime()));
                        task.setFinishTime(addTime(parse(task.getStartTime()), parse(task.getDuration())).toString());
                        if (!completeTaskBeforeShiftEnd(task)) {
                            nextShiftTasks.add(task);
                        }
                    } else {
                        task.setStartTime(addTime(parse(currentTask.getFinishTime()), parse(FIVE_MINUTES)).toString());
                        task.setFinishTime(addTime(parse(task.getStartTime()), parse(task.getDuration())).toString());
                        if (!completeTaskBeforeShiftEnd(task)) {
                            nextShiftTasks.add(task);
                        }
                    }
                }
            }
            lastTask = task;
        }

        if (nextShiftTasks != null) {
            moveRemainingTasks(nextShiftTasks);
        }
        return tasks;
    }

    private void moveRemainingTasks(List<Task> nextShiftTasks) {
        for (Task task : nextShiftTasks) {
            assignTask(task, NOT_INITIALIZED);
        }
    }

    private Task findIntersectingTask(List<Task> tasks, Task task) {
        Task intersectingTask = new Task();
        if (tasks != null) {
            for (Task task1 : tasks) {
                if (!notIntersecting(task, task1)) {
                    intersectingTask = task1;
                }
            }
        }
        return intersectingTask;
    }

    private List<Task> bubbleSortByPriority(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                Task tempTask = null;
                if (tasks.get(i).getPriority() > tasks.get(j).getPriority()) {
                    tempTask = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, tempTask);
                }
            }
        }
        return tasks;
    }

    public List<Task> equalizeTasks(List<EmployeeDTO> employeesDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        List<Task> mediumTasks = taskRepository.findByPriorityAndTimePlacedStartingWith(MEDIUM_PRIORITY, today);
        List<Task> lowTasks = taskRepository.findByPriorityAndTimePlacedStartingWith(LOW_PRIORITY, today);
        List<Task> allTasks = new ArrayList<>();

        mediumTasks = equalize(employeesDTO, mediumTasks);
        lowTasks = equalize(employeesDTO, lowTasks);
        allTasks.addAll(mediumTasks);
        allTasks.addAll(lowTasks);
        return allTasks;
    }

    private List<Task> equalize(List<EmployeeDTO> employeesDTO, List<Task> tasks) {
        LocalTime afterOneHour = new LocalTime();
        afterOneHour = afterOneHour.plusHours(1);

        tasks = bubbleSortByDuration(tasks);

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getStartTime() == null) {
                tasks.remove(i);
            }
            if (parse(tasks.get(i).getStartTime()).isBefore(afterOneHour)) {
                tasks.remove(i);
            }
        }

        for (int i = 0; i < tasks.size(); i++) {
            taskRepository.save(assignToLeastBusy(tasks.get(i), employeesDTO));

        }
//        for (Task task : tasks) {
//            employeesDTO taskRepository.save(assignToLeastBusy(task, employeesDTO));
//        }
        return tasks;
    }

    private List<Task> bubbleSortByDuration(List<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            for (int j = i + 1; j < tasks.size(); j++) {
                Task tempTask = null;
                if (parse(tasks.get(i).getDuration()).isAfter(parse(tasks.get(j).getDuration()))) {
                    tempTask = tasks.get(i);
                    tasks.set(i, tasks.get(j));
                    tasks.set(j, tempTask);
                }
            }
        }
        return tasks;
    }

    private Task assignAccordingToPriority(Task task, List<EmployeeDTO> employees) {
        EmployeeDTO leastBusy = null;
        int priorityToFind = task.getPriority();

        for (EmployeeDTO employeeDTO : employees) {
            if (leastBusy == null) {
                leastBusy = employeeDTO;
            } else if (parse(leastBusy.getSumDurationForPriority(priorityToFind)).
                    isAfter(parse(employeeDTO.getSumDurationForPriority(priorityToFind)))) {
                leastBusy = employeeDTO;
            } else if (parse(leastBusy.getSumDurationForPriority(priorityToFind)).
                    equals(parse(employeeDTO.getSumDurationForPriority(priorityToFind)))) {
                if (parse(leastBusy.getSumDuration()).isAfter(parse(employeeDTO.getSumDuration()))) {
                    leastBusy = employeeDTO;
                }
            }
        }
        task.setAssignee(leastBusy.getWorkingSchedule().getEmployee());
        return task;
    }

    private LocalTime parse(String dateToParse) {
        return LocalTime.parse(dateToParse);
    }
}

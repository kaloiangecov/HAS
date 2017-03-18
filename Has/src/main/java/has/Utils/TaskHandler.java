package has.Utils;

import has.Employee.Employee;
import has.Employee.EmployeeRepository;
import has.Request.Request;
import has.Request.RequestRepository;
import has.Task.Task;
import has.Task.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

/**
 * Created by kaloi on 3/13/2017.
 */
@Component
public class TaskHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TaskRepository taskRepository;

    private static final int TASK_STATUS_CREATED = 1;


    public Task createTaskFromRequest(Request request) {
        Task task = new Task();
        task.setTitle("Request " + request.getId());
        task.setDescription(createDescription(request));
        task.setRequest(request);
        task.setAssigner("SYSTEM");
        task.setTimePlaced(request.getTimePlaced());
        task.setPriority(2);
        task = assignTask(task);
        //TODO set description, employee, duration and target time(евентуално)
        return taskRepository.save(task);
    }

    public Task assignTask(Task task) {
        List<Employee> employees = getAvailableEmployees();
        List<Employee> availableEmployees = null;
        Employee assignee;

        for (Employee employee : employees) {
            if (taskRepository.findByAssigneeIdAndStatusNot(employee.getId(), TASK_STATUS_CREATED) == null) {
//                if (employee.s)
                availableEmployees.add(employee);
            }
        }

        if (!availableEmployees.isEmpty()) {
            assignee = getRandomEmployee(availableEmployees);
            task.setAssignee(assignee);
        }
        return task;
    }

    public String createDescription(Request req){
        StringBuilder description = new StringBuilder();
        description
                    .append("Room request: "+ req.getId() + System.lineSeparator())
                    .append("Type of request: " + req.getType())
                    .append("From room: "+req.getReservationGuest().getRoom());
        if(req.getMealRequests() != null){
            description.append("Meals requested: " +req.getMealRequests());
        }
        return description.toString();
    }

    private List<Employee> getAvailableEmployees() {
        return employeeRepository.findByBusyFalse();
    }

    private Employee getRandomEmployee(List<Employee> employees) {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(employees.size());
        return employees.get(index);
    }
}

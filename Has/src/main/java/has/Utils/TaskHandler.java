package has.Utils;

import has.Employee.Employee;
import has.Employee.EmployeeRepository;
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

    public void assignTask(Task task) {
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

package has.Utils;

import has.Employee.Employee;
import has.Employee.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kaloi on 3/13/2017.
 */
@Component
public class TaskHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    private List<Employee> getAvailableEmployees() {
        return employeeRepository.findByBusyFalse();
    }
}

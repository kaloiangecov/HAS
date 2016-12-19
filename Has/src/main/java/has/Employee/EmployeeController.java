package has.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kaloi on 12/19/2016.
 */
@RestController
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/emploee", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee save(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee findEmployeeById(@PathVariable Long id) throws Exception {
        return employeeService.findById(id);
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee removeEmployeeById(@PathVariable Long id) throws Exception {
        return employeeService.remove(id);
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) throws Exception {
        return employeeService.update(id, employee);
    }
}

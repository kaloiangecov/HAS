package has.Employee;

import has.Utils.DataTableResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by kaloi on 12/19/2016.
 */
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/employee", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_EMPLOYEE')")
    public Employee save(@RequestBody @Valid Employee employee) throws Exception {
        return employeeService.save(employee);
    }

    @RequestMapping(value = "/employees", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_EMPLOYEE')")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @RequestMapping(value = "/employees/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_SEARCH_EMPLOYEE')")
    public
    @ResponseBody
    DataTableResult searchEmployees(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        char sortColumnNumber = parameterMap.get("order[0][column]")[0].charAt(0);
        String sortColumnParam = "columns[" + sortColumnNumber + "][data]";

        Page<Employee> employees = employeeService.searchEmployees(
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get(sortColumnParam)[0],
                parameterMap.get("order[0][dir]")[0],
                parameterMap.get("fullName")[0],
                parameterMap.get("phone")[0],
                parameterMap.get("dateHired")[0],
                Boolean.parseBoolean(parameterMap.get("showDisabled")[0]));

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                employees.getTotalElements(),
                employees.getTotalElements(),
                employees.getContent());
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_EMPLOYEE')")
    public Employee findEmployeeById(@PathVariable Long id) throws Exception {
        return employeeService.findById(id);
    }

    @RequestMapping(value = "/employee/by-user/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_EMPLOYEE')")
    public Employee findEmployeeByUserId(@PathVariable Long id) throws Exception {
        return employeeService.findByUserId(id);
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_EMPLOYEE')")
    public Employee removeEmployeeById(@PathVariable Long id) throws Exception {
        return employeeService.remove(id);
    }

    @RequestMapping(value = "/employee/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_EMPLOYEE')")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) throws Exception {
        return employeeService.update(id, employee);
    }

    @RequestMapping(value = "/employee/employed/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_EMPLOYEE')")
    public Employee changeStatus(@PathVariable Long id) throws Exception {
        return employeeService.changeEmployment(id);
    }

    @RequestMapping(value = "/employees/shift", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_EMPLOYEE')")
    public List<Employee> findEmployeesForShift(@RequestParam("date") String date, @Valid @RequestParam("shift") int shift) {
        return employeeService.findEmployeesForShift(date, shift);
    }

    @RequestMapping(value = "/employees/service/shift", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_EMPLOYEE')")
    public List<Employee> findServiceEmployeesForShift(@RequestParam("date") String date, @Valid @RequestParam("shift") int shift) {
        return employeeService.findServiceEmployeesForShift(date, shift);
    }
}

package has.Task;

import has.Employee.Employee;
import has.Employee.EmployeeService;
import has.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by kaloi on 2/24/2017.
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService service;

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/tasks", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_TASK')")
    public Task save(@RequestBody @Valid Task Task, @AuthenticationPrincipal User user) {
        return service.save(Task, user.getUsername());
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_TASK')")
    public List<Task> getAllTasks() {
        return service.getAllTasks();
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_TASK')")
    public Task findTaskById(@PathVariable Long id) throws Exception {
        return service.findById(id);
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_TASK')")
    public Task removeTaskById(@PathVariable Long id) throws Exception {
        return service.remove(id);
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_TASK')")
    public Task updateRequest(@PathVariable Long id, @RequestBody @Valid Task task) throws Exception {
        return service.update(id, task);
    }

    @RequestMapping(value = "/tasks/own", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getOwnTasks(@AuthenticationPrincipal User user) throws Exception {
        Employee employee = employeeService.findByUserId(user.getId());
        return service.getEmployeesTasks(employee);
    }

    @RequestMapping(value = "/tasks/{taskId}/{status}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('PERM_EDIT_TASK')")
    public Task changeTaskStatus(@PathVariable Long taskId, @PathVariable Integer status) throws Exception {
        return service.changeStatus(taskId,status);
    }

}

package has.Task;

import has.Employee.Employee;
import has.Employee.EmployeeService;
import has.User.User;
import has.Utils.DataTableResult;
import has.Utils.TaskHandler;
import has.Utils.TimeFormatter;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by kaloi on 2/24/2017.
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService service;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TaskHandler taskHandler;

    private TimeFormatter timeFormatter;

    @RequestMapping(value = "/tasks", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_TASK')")
    public Task save(@RequestBody @Valid Task Task, @AuthenticationPrincipal User user) {
        return service.save(Task, user.getUsername());
    }

    @RequestMapping(value = "/tasks/automated-assign", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_TASK')")
    public Task saveAutomatically(@RequestBody @Valid Task Task) throws Exception {
        return service.save(Task);
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
        return service.changeStatus(taskId, status);
    }

    @RequestMapping(value = "/tasks/unresolved", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getOwnUncompletedTasks(@AuthenticationPrincipal User user) throws Exception {
        Employee employee = employeeService.findByUserId(user.getId());
        return service.getEmployeesUnresolvedTasks(employee);
    }

    @RequestMapping(value = "/tasks/current", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public DataTableResult getCurrentTasks(HttpServletRequest request, @AuthenticationPrincipal User user) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        char sortColumnNumber = parameterMap.get("order[0][column]")[0].charAt(0);
        String sortColumnParam = "columns[" + sortColumnNumber + "][data]";

        Page<Task> tasks = service.searchCurrentShift(
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get(sortColumnParam)[0],
                parameterMap.get("order[0][dir]")[0],
                parameterMap.get("assignee")[0]
        );

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                tasks.getTotalElements(),
                tasks.getTotalElements(),
                tasks.getContent());
    }


    //TODO: from here on is tests

    @RequestMapping(value = "/tasks/organise/{employeeId}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_TASK')")
    public List<Task> organiseTasks(@PathVariable Long employeeId) throws Exception {
        return service.organizeTasks(employeeId);
    }

    @RequestMapping(value = "/tasks/equalize", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_TASK')")
    public List<Task> equalizeTasks() throws Exception {
        return service.equalize();
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_TASK')")
    public String test() throws Exception {
        Date date = new Date();

        String result = "today: " + date.toString() + " tomorrow: " + timeFormatter.getTomorrowDate();
        return result;
    }

}

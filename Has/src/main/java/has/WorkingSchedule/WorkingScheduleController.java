package has.WorkingSchedule;

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
 * Created by Chokleet on 20.12.2016 г..
 */
@RestController
public class WorkingScheduleController {

    @Autowired
    private WorkingScheduleService wsService;

    @RequestMapping(value = "/schedule", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_SCHEDULE')")
    public WorkingSchedule save(@RequestBody @Valid WorkingSchedule schedule) {
        return wsService.save(schedule);
    }

    @RequestMapping(value = "/schedules", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<WorkingSchedule> getAllSchedules() {
        return wsService.getAllSchedules();
    }

    @RequestMapping(value = "/schedules/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_SEARCH_EMPLOYEE')")
    public
    @ResponseBody
    DataTableResult searchSchedules(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        char sortColumnNumber = parameterMap.get("order[0][column]")[0].charAt(0);
        String sortColumnParam = "columns[" + sortColumnNumber + "][data]";

        Page<WorkingSchedule> schedules = wsService.searchSchedule(
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get(sortColumnParam)[0],
                parameterMap.get("order[0][dir]")[0],
                parameterMap.get("startDate")[0],
                parameterMap.get("endDate")[0],
                Long.parseLong(parameterMap.get("roleID")[0]));

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                schedules.getTotalElements(),
                schedules.getTotalElements(),
                schedules.getContent());
    }

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_SEARCH_SCHEDULE')")
    public WorkingSchedule getScheduleById(@PathVariable Long id) throws Exception {
        return wsService.findById(id);
    }

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_SCHEDULE')")
    public WorkingSchedule removeScheduleById(@PathVariable Long id) throws Exception {
        return wsService.remove(id);
    }

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_SCHEDULE')")
    public WorkingSchedule updateSchedule(@PathVariable Long id, @RequestBody @Valid WorkingSchedule schedule) throws Exception {
        return wsService.update(id, schedule);
    }
}

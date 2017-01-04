package has.WorkingSchedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasAuthority('ADMIN')")
    public WorkingSchedule save(@RequestBody WorkingSchedule schedule) {
        return wsService.save(schedule);
    }

    @RequestMapping(value = "/schedules", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<WorkingSchedule> getAllSchedules() {
        return wsService.getAllSchedules();
    }

    @RequestMapping(value = "/schedules/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public WorkingSchedule getScheduleById(@PathVariable Long id) throws Exception {
        return wsService.findById(id);
    }

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public WorkingSchedule removeScheduleById(@PathVariable Long id) throws Exception {
        return wsService.remove(id);
    }

    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public WorkingSchedule updateSchedule(@PathVariable Long id, @RequestBody WorkingSchedule schedule) throws Exception {
        return wsService.update(id, schedule);
    }
}

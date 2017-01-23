package has.Guest;

import has.DataTableResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Created by kaloi on 12/20/2016.
 */
@RestController
public class GuestController {

    @Autowired
    private GuestService guestService;

    @RequestMapping(value = "/guest", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Guest save(@RequestBody @Valid Guest guest) {
        return guestService.save(guest);
    }

    @RequestMapping(value = "/guests", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
    }

    @RequestMapping(value = "/searchguests", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public @ResponseBody
    DataTableResult searchGuests(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        List<Guest> guests = guestService.searchGuests(
            parameterMap.get("fullName")[0],
            parameterMap.get("phone")[0]
        );

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                guests.size(),
                guests.size(),
                guests);
    }

    @RequestMapping(value = "/guest/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Guest findGuestById(@PathVariable Long id) throws Exception {
        return guestService.findById(id);
    }

    @RequestMapping(value = "/guest/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Guest removeGuestById(@PathVariable Long id) throws Exception {
        return guestService.remove(id);
    }

    @RequestMapping(value = "/guest/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Guest updateGuest(@PathVariable Long id, @RequestBody @Valid Guest guest) throws Exception {
        return guestService.update(id, guest);
    }
}

package has.Guest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Guest save(@RequestBody Guest guest) {
        return guestService.save(guest);
    }

    @RequestMapping(value = "/guests", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
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
    public Guest updateGuest(@PathVariable Long id, @RequestBody Guest guest) throws Exception {
        return guestService.update(id, guest);
    }
}

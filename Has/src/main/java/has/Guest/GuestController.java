package has.Guest;

import has.User.User;
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
    @PreAuthorize("hasAuthority('PERM_CREATE_GUEST')")
    public Guest save(@RequestBody @Valid Guest guest) throws Exception {
        return guestService.save(guest);
    }

    @RequestMapping(value = "/guests", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_GUEST')")
    public List<Guest> getAllGuests() {
        return guestService.getAllGuests();
    }

    @RequestMapping(value = "/guests/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_SEARCH_GUEST')")
    public
    @ResponseBody
    DataTableResult searchGuests(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        char sortColumnNumber = parameterMap.get("order[0][column]")[0].charAt(0);
        String sortColumnParam = "columns[" + sortColumnNumber + "][data]";

        Page<Guest> guests = guestService.searchGuests(
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get(sortColumnParam)[0],
                parameterMap.get("order[0][dir]")[0],
                parameterMap.get("fullName")[0],
                parameterMap.get("phone")[0]
        );

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                guests.getTotalElements(),
                guests.getTotalElements(),
                guests.getContent());
    }

    @RequestMapping(value = "/guests/free/{reservationId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_GUEST')")
    public List<Guest> findReservationFreeGuests(@PathVariable Long reservationId) {
        return guestService.findReservationFreeGuests(reservationId);
    }

    @RequestMapping(value = "/guest/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_GUEST')")
    public Guest findGuestById(@PathVariable Long id) throws Exception {
        return guestService.findById(id);
    }

    @RequestMapping(value = "/guest/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_GUEST')")
    public Guest removeGuestById(@PathVariable Long id) throws Exception {
        return guestService.remove(id);
    }

    @RequestMapping(value = "/guest/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_GUEST')")
    public Guest updateGuest(@PathVariable Long id, @RequestBody @Valid Guest guest) throws Exception {
        return guestService.update(id, guest);
    }

    @RequestMapping(value = "/guest/by-email", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    //@PreAuthorize("hasAuthority('PERM_VIEW_USER')")
    public Guest findByUserEmail(@RequestBody User user) throws Exception {
        return guestService.findByUserEmail(user.getEmail());
    }
}

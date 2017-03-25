package has.Request;

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
 * Created by kaloi on 12/20/2016.
 */
@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @RequestMapping(value = "/request", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Request save(@RequestBody @Valid Request request, @AuthenticationPrincipal User user) throws Exception {
        return requestService.save(request, user);
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_REQUEST')")
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @RequestMapping(value = "/request/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_REQUEST')")
    public Request findRequestById(@PathVariable Long id) throws Exception {
        return requestService.findById(id);
    }

    @RequestMapping(value = "/request/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_REQUEST')")
    public Request removeRequestById(@PathVariable Long id) throws Exception {
        return requestService.remove(id);
    }

    @RequestMapping(value = "/request/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_REQUEST')")
    public Request updateRequest(@PathVariable Long id, @RequestBody @Valid Request request) throws Exception {
        return requestService.update(id, request);
    }

    @RequestMapping(value = "/requests/reservation/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_REQUEST')")
    public List<Request> findRequestByReservation(@PathVariable Long id) throws Exception {
        return requestService.findByReservationId(id);
    }
}

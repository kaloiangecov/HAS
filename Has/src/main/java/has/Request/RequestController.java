package has.Request;

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
public class RequestController {

    @Autowired
    private RequestService requestService;

    @RequestMapping(value = "/request", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Request save(@RequestBody Request request) {
        return requestService.save(request);
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Request> getAllRequests() {
        return requestService.getAllRequests();
    }

    @RequestMapping(value = "/request/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Request findRequestById(@PathVariable Long id) throws Exception {
        return requestService.findById(id);
    }

    @RequestMapping(value = "/request/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Request removeRequestById(@PathVariable Long id) throws Exception {
        return requestService.remove(id);
    }

    @RequestMapping(value = "/request/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Request updateRequest(@PathVariable Long id, @RequestBody Request request) throws Exception {
        return requestService.update(id, request);
    }
}

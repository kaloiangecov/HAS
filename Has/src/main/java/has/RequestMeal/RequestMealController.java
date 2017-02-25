package has.RequestMeal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by kaloi on 2/24/2017.
 */
@RestController
public class RequestMealController {

    @Autowired
    private RequestMealService requestMealService;

    @RequestMapping(value = "/request-meals", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_REQUEST_MEAL')")
    public RequestMeal save(@RequestBody @Valid RequestMeal requestMeal) {
        return requestMealService.save(requestMeal);
    }

    @RequestMapping(value = "/request-meals", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_REQUEST_MEAL')")
    public List<RequestMeal> getAllRequests() {
        return requestMealService.getAllRequests();
    }

    @RequestMapping(value = "/request-meals/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_REQUEST_MEAL')")
    public RequestMeal findRequestById(@PathVariable Long id) throws Exception {
        return requestMealService.findById(id);
    }

    @RequestMapping(value = "/request-meals/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_REQUEST_MEAL')")
    public RequestMeal removeRequestById(@PathVariable Long id) throws Exception {
        return requestMealService.remove(id);
    }

    @RequestMapping(value = "/request-meals/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_REQUEST_MEAL')")
    public RequestMeal updateRequest(@PathVariable Long id, @RequestBody @Valid RequestMeal requestMeal) throws Exception {
        return requestMealService.update(id, requestMeal);
    }

    @RequestMapping(value = "/request-meals/expenses/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_REQUEST_MEAL')")
    public List<RequestMeal> getAllGuestExpenses(@PathVariable Long id) {
        return requestMealService.findGuestExpenses(id);
    }
}

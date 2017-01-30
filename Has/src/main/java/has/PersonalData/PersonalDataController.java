package has.PersonalData;

import has.Exceptions.IdentityNumberAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by kaloi on 1/2/2017.
 */
@RestController
public class PersonalDataController {

    @Autowired
    private PersonalDataService personalDataService;

    @RequestMapping(value = "/personal-data", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_PERSONAL_DATA')")
    public PersonalData save(@RequestBody @Valid PersonalData personalData) throws IdentityNumberAlreadyExists {
        return personalDataService.save(personalData);
    }

    @RequestMapping(value = "/data", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_PERSONAL_DATA')")
    public List<PersonalData> getAllEmployees() {
        return personalDataService.getAllPersonalData();
    }

    @RequestMapping(value = "/personal-data/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_PERSONAL_DATA')")
    public PersonalData findEmployeeById(@PathVariable Long id) throws Exception {
        return personalDataService.findById(id);
    }

    @RequestMapping(value = "/personal-data/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_PERSONAL_DATA')")
    public PersonalData removeEmployeeById(@PathVariable Long id) throws Exception {
        return personalDataService.remove(id);
    }

    @RequestMapping(value = "/personal-data/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_PERSONAL_DATA')")
    public PersonalData updateEmployee(@PathVariable Long id, @RequestBody @Valid PersonalData personalData) throws Exception {
        return personalDataService.update(id, personalData);
    }
}

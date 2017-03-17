package has.Configuration.Settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by kaloi on 3/16/2017.
 */
@RestController
public class HasConfigurationController {

    @Autowired
    private HasConfigurationService hasConfigurationService;

    @RequestMapping(value = "/configurations", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_CREATE_CONFIGURATION')")
    public HasConfiguration save(@RequestBody @Valid HasConfiguration hasConfiguration) {
        return hasConfigurationService.save(hasConfiguration);
    }

    @RequestMapping(value = "/configurations", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_CONFIGURATION')")
    public List<HasConfiguration> getAllConfigurations() {
        return hasConfigurationService.getAllConfigurations();
    }

    @RequestMapping(value = "/configurations/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_CONFIGURATION')")
    public HasConfiguration findConfigurationById(@PathVariable Long id) throws Exception {
        return hasConfigurationService.findById(id);
    }

    @RequestMapping(value = "/configurations/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_CONFIGURATION')")
    public HasConfiguration removeConfigurationById(@PathVariable Long id) throws Exception {
        return hasConfigurationService.remove(id);
    }

    @RequestMapping(value = "/configurations/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_CONFIGURATION')")
    public HasConfiguration updateConfiguration(@PathVariable Long id, @RequestBody @Valid HasConfiguration hasConfiguration) throws Exception {
        return hasConfigurationService.update(id, hasConfiguration);
    }

    @RequestMapping(value = "/configurations/init/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_CONFIGURATION')")
    public HasConfiguration setActiveConfiguration(@PathVariable Long id) throws Exception {
        return hasConfigurationService.setActiveConfiguration(id);
    }
}

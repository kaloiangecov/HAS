package has.Roles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by kaloi on 1/30/2017.
 */
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/role", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole save(@RequestBody @Valid UserRole role) throws Exception {
        return roleService.save(role);
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_ROLE')")
    public List<UserRole> getAllRoles() {
        return roleService.findAll();
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_ROLE')")
    public UserRole findRoleByNumber(@PathVariable Long id) throws Exception {
        return roleService.findById(id);
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole removeRoleById(@PathVariable Long id) throws Exception {
        return roleService.remove(id);
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole updateRole(@PathVariable Long id, @RequestBody @Valid UserRole role) throws Exception {
        return roleService.update(role, id);
    }
}

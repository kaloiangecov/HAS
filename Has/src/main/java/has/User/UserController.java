package has.User;

import has.DataTableResult;
import has.Exceptions.UserAlreadyExists;
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
 * Created by kaloi on 12/17/2016.
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    RoleRepository repoRole;

    @RequestMapping(value = "/user", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public User save(@RequestBody @Valid User user) throws UserAlreadyExists {
        return userService.save(user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers(HttpServletRequest request) throws Exception {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/roles", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserRole> getAllRoles() throws Exception {
        return userService.getAllRoles();
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserRole findRoleById(@PathVariable Long id) throws Exception {
        return userService.findRoleById(id);
    }

    @RequestMapping(value = "/searchusers", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public
    @ResponseBody
    DataTableResult searchUsers(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        Page<User> users = userService.searchUsers(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get("username")[0],
                parameterMap.get("email")[0],
                Integer.parseInt(parameterMap.get("role")[0])
        );

        //List<User> users = userService.getAllUsers();

        return new DataTableResult(
                Integer.parseInt(parameterMap.get("draw")[0]),
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                users.getTotalElements(),
                users.getTotalElements(),
                users.getContent());
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findUserById(@PathVariable Long id) throws Exception {
        return userService.findById(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public User removeUserById(@PathVariable Long id) throws Exception {
        return userService.remove(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public User updateUser(@PathVariable Long id, @RequestBody @Valid User user) throws Exception {
        return userService.update(id, user);
    }
}

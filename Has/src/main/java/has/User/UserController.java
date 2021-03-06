package has.User;

import has.Exceptions.EmailAlreadyExists;
import has.Exceptions.UserAlreadyExists;
import has.Roles.RoleRepository;
import has.Utils.DataTableResult;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @PreAuthorize("hasAuthority('PERM_CREATE_USER')")
    public User save(@RequestBody @Valid User user) throws UserAlreadyExists, EmailAlreadyExists {
        return userService.save(user);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_USER')")
    public List<User> getAllUsers() throws Exception {
        return userService.getAllUsers();
    }

    @RequestMapping(value = "/users/free-employees/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_USER')")
    public List<User> getFreeEmployeeUsers(@PathVariable Long id) throws Exception {
        return userService.findFreeUsers(id, 1);
    }

    @RequestMapping(value = "/users/free-guests/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_VIEW_USER')")
    public List<User> getFreeGuestUsers(@PathVariable Long id) throws Exception {
        return userService.findFreeUsers(id, 2);
    }

    @RequestMapping(value = "/users/search", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_SEARCH_USER')")
    public
    @ResponseBody
    DataTableResult searchUsers(HttpServletRequest request) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();

        char sortColumnNumber = parameterMap.get("order[0][column]")[0].charAt(0);
        String sortColumnParam = "columns[" + sortColumnNumber + "][data]";

        Page<User> users = userService.searchUsers(
                Integer.parseInt(parameterMap.get("start")[0]),
                Integer.parseInt(parameterMap.get("length")[0]),
                parameterMap.get(sortColumnParam)[0],
                parameterMap.get("order[0][dir]")[0],
                parameterMap.get("username")[0],
                parameterMap.get("email")[0],
                Long.parseLong(parameterMap.get("roleID")[0])
        );

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
    @PreAuthorize("hasAuthority('PERM_VIEW_USER')")
    public User findUserById(@PathVariable Long id) throws Exception {
        return userService.findById(id);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Principal login(Principal user, HttpServletRequest request) throws Exception {
        User dbUser = userService.findByUsername(user.getName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userService.updateLastLogin(dbUser.getId(), sdf.format(new Date()));

        return user;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_USER')")
    public User removeUserById(@PathVariable Long id) throws Exception {
        return userService.remove(id);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_EDIT_USER')")
    public User updateUser(@PathVariable Long id, @RequestBody @Valid User user) throws Exception {
        return userService.update(id, user);
    }

    @RequestMapping(value = "/user/enabled/{id}", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("hasAuthority('PERM_REMOVE_EMPLOYEE')")
    public User changeStatus(@PathVariable Long id) throws Exception {
        return userService.changeEnabled(id);
    }
}

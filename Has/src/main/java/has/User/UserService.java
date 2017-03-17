package has.User;

import has.Exceptions.EmailAlreadyExists;
import has.Exceptions.UserAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaloi on 12/17/2016.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int EMPLOYEE_TYPE = 1;
    private static final int GUEST_TYPE = 2;

    public User save(User user) throws UserAlreadyExists, EmailAlreadyExists {
        validateAlreadyExists(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public User update(Long id, User user) throws Exception {
        User dbUser = repo.findOne(id);
        validateIdNotNull(dbUser);
        validateAlreadyExists(user);

        dbUser.setLastLogin(user.getLastLogin());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        dbUser.setRegDate(user.getRegDate());
        dbUser.setUsername(user.getUsername());
        dbUser.setEmail(user.getEmail());
        dbUser.setUserRole(user.getUserRole());

        return repo.save(dbUser);
    }

    public User updateLastLogin(Long id, String lastLogin) throws Exception {
        User user = repo.findOne(id);
        validateIdNotNull(user);

        user.setLastLogin(lastLogin);
        return repo.save(user);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public Page<User> searchUsers(int start, int length, String sortColumn, String sortDirection, String username, String email, Long roleID) {

        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);

        if (roleID != -1)
            return repo.findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUserRoleId(username, email, roleID, request);
        else
            return repo.findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(username, email, request);
    }

    public List<User> findFreeUsers(Long id, int type) {
        List<User> users = new ArrayList<User>();

        switch (type) {
            case EMPLOYEE_TYPE:
                users = repo.findFreeEmployeeUsers();
                break;
            case GUEST_TYPE:
                users = repo.findFreeGuestUsers();
                break;
        }

        if (id > 0) {
            User dbUser = repo.findOne(id);
            if (dbUser != null)
                users.add(dbUser);
        }

        return users;
    }

    public User findById(Long id) throws Exception {
        User user = repo.findOne(id);
        validateIdNotNull(user);
        return user;
    }

    public User findByEmail(String email) throws Exception {
        User user = repo.findByEmail(email);
        validateIdNotNull(user);
        return user;
    }

    public User findByUsername(String username) throws Exception {
        User user = repo.findByUsername(username);
        validateIdNotNull(user);
        return user;
    }

    public User remove(Long id) throws Exception {
        User user = repo.findOne(id);
        validateIdNotNull(user);
        repo.delete(user);
        return user;
    }

    public User changeEnabled(Long id) throws Exception {
        User user = repo.findOne(id);
        validateIdNotNull(user);

        user.setEnabled(!user.isEnabled());

        return repo.save(user);
    }

    public void validateAlreadyExists(User user) throws UserAlreadyExists, EmailAlreadyExists {
        User dbUser1 = repo.findByUsername(user.getUsername());
        if (dbUser1 != null && dbUser1.getId() != user.getId()) {
            throw new UserAlreadyExists(user.getUsername());
        }

        User dbUser2 = repo.findByEmail(user.getEmail());
        if (dbUser2 != null && dbUser2.getId() != user.getId()) {
            throw new EmailAlreadyExists(user.getEmail());
        }
    }

    private void validateIdNotNull(User user) throws Exception {
        if (user == null) {
            throw new Exception("There is no user with such ID");
        }
    }
}
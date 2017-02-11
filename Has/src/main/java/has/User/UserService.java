package has.User;

import has.Exceptions.UserAlreadyExists;
import has.Roles.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/17/2016.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private RoleRepository repoRole;

    public User save(User user) throws UserAlreadyExists {
        if (repo.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExists(user.getUsername());
        }
        return repo.save(user);
    }

    public User update(Long id, User user) throws Exception {
        User dbUser = repo.findOne(id);
        if (dbUser == null) {
            throw new Exception("There is no user with such ID");
        }

        User dbUserCheck = repo.findByUsername(user.getUsername());
        if ((dbUserCheck != null) && (dbUserCheck.getId() != user.getId())) {
            throw new UserAlreadyExists(user.getUsername());
        }

        dbUser.setLastLogin(user.getLastLogin());
        dbUser.setPassword(user.getPassword());
        dbUser.setRegDate(user.getRegDate());
        dbUser.setUsername(user.getUsername());
        dbUser.setEmail(user.getEmail());
        dbUser.setUserRole(user.getUserRole());

        return repo.save(dbUser);
    }

    public User updateLastLogin(Long id, String lastLogin) throws Exception {
        User dbUser = repo.findOne(id);
        if (dbUser == null) {
            throw new Exception("There is no user with such ID");
        }

        dbUser.setLastLogin(lastLogin);

        return repo.save(dbUser);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public Page<User> searchUsers(int start, int length, String username, String email, Long roleID) {

        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");

        return repo.findByUsernameContainingAndEmailContainingAndUserRoleId(username, email, roleID, request);
    }

    public User findById(Long id) throws Exception {
        User dbUser = repo.findOne(id);
        if (dbUser == null) {
            throw new Exception("There is no user with such ID");
        }
        return dbUser;
    }

    public User findByUsername(String username) throws Exception {
        User dbUser = repo.findByUsername(username);
        if (dbUser == null) {
            throw new Exception("There is no user with such username");
        }
        return dbUser;
    }

    public User remove(Long id) throws Exception {
        User dbUser = repo.findOne(id);
        if (dbUser == null) {
            throw new Exception("There is no user with such ID");
        }
        repo.delete(dbUser);
        return dbUser;
    }
}
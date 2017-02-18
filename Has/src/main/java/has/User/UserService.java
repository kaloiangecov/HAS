package has.User;

import has.Exceptions.EmailAlreadyExists;
import has.Exceptions.UserAlreadyExists;
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

    public User save(User user) throws UserAlreadyExists, EmailAlreadyExists {
        validateAlreadyExists(user);
        return repo.save(user);
    }

    public User update(Long id, User user) throws UserAlreadyExists, EmailAlreadyExists, Exception {
        User dbUser = repo.findOne(id);
        if (dbUser == null) {
            throw new Exception("There is no user with such ID");
        }
        validateAlreadyExists(user);

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

    public Page<User> searchUsers(int start, int length, String sortColumn, String sortDirection, String username, String email, Long roleID) {

        PageRequest request = new PageRequest((start / length), length, Sort.Direction.fromString(sortDirection), sortColumn);

        return repo.findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUserRoleId(username, email, roleID, request);
    }

    public List<User> findFreeUsers(Long id) {
        List<User> users = repo.findFreeUsers();

        if (id > 0) {
            User dbUser = repo.findOne(id);
            if (dbUser != null)
                users.add(dbUser);
        }

        return users;
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
    
}
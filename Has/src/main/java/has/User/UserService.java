package has.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 12/17/2016.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User save(User user){
        return repo.save(user);
    }

    public User update(Long id, User user) throws Exception {
        User dbUser = repo.findOne(id);
        if(dbUser == null){
            throw new Exception("There is no user with such ID");
        }

        dbUser.setLastLogin(user.getLastLogin());
        dbUser.setPassword(user.getPassword());
        dbUser.setRegDate(user.getRegDate());
        dbUser.setRole(user.getRole());
        dbUser.setUsername(user.getUsername());

        return repo.save(dbUser);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public User findById(Long id) throws Exception {
        User dbUser = repo.findOne(id);
        if(dbUser == null){
            throw new Exception("There is no user with such ID");
        }
        return dbUser;
    }

    public User remove(Long id) throws Exception {
        User dbUser = repo.findOne(id);
        if(dbUser == null){
            throw new Exception("There is no user with such ID");
        }
        repo.delete(dbUser);
        return dbUser;
    }
}
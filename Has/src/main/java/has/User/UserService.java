package has.User;

import has.Employee.Employee;
import has.Exceptions.UserAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaloi on 12/17/2016.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    public User save(User user) throws UserAlreadyExists {
        if(repo.findByUsername(user.getUsername()) != null){
            throw new UserAlreadyExists(user.getUsername());
        }
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

    @PostConstruct
    private void initSomeData(){
        Employee emp1 = new Employee("yday",5);

        RolePermission perm1 = new RolePermission("ADMIN");
        List<RolePermission> permissions = new ArrayList<>();
        permissions.add(perm1);
        UserRole role1 = new UserRole("adm", permissions);

        User usr1 = new User("never", "password", "yesterday", "ivan", emp1, role1);

        repo.save(usr1);
    }
}
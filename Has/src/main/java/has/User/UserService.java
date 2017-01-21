package has.User;

import has.Employee.Employee;
import has.Employee.EmployeeRepository;
import has.Exceptions.UserAlreadyExists;
import has.Guest.GuestRepository;
import has.PersonalData.PersonalData;
import has.Guest.Guest;
import has.Room.Room;
import has.Room.RoomRepository;
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

    @Autowired
    private EmployeeRepository repoEmployee;

    @Autowired
    private GuestRepository repoGuest;

    @Autowired
    private RoomRepository repoRoom;

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

    public List<User> searchUsers(int draw, int start, int length, String username, String email) {
        List<User> users = repo.findByUsernameAndEmail(username, email);
        return users;
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

        RolePermission perm1 = new RolePermission("ADMIN");
        List<RolePermission> permissions = new ArrayList<>();
        permissions.add(perm1);
        UserRole role1 = new UserRole("adm", permissions);

        User usr1 = new User("2017-01-10T12:30:00", "password", "shit@abv.bg", "2016-11-12T11:30:30", "ivan", role1);
        User usr2 = new User("2017-01-11T12:30:00", "123456789", "dick@abv.bg", "2016-09-12T11:30:30", "grigor", new UserRole("adm", permissions));

        Employee emp1 = new Employee(
                "2016-11-12T11:30:30",
                5,
                usr1,
                new PersonalData(
                        "some address",
                        "9203318220",
                        "Stefan Nedelchev",
                        "2020-12-10",
                        "2010-12-10",
                        "МВР Търговище",
                        "123456123",
                        "102391203"));

        Employee emp2 = new Employee(
                "2016-08-25T12:23:30",
                5,
                usr1,
                new PersonalData(
                        "somewhere",
                        "8888881122",
                        "Валери Божинов",
                        "2021-07-10",
                        "2011-07-10",
                        "МВР Някъде си",
                        "000777111",
                        "567812345"));

        Guest guest1 = new Guest(
                0,
                usr1,
                new PersonalData(
                        "some address 2",
                        "9412567890",
                        "Христина Христова",
                        "2020-12-11",
                        "2010-12-11",
                        "МВР Варна",
                        "888000555",
                        "4543212453"));

        Room room1 = new Room(1, 1, 104, 2, 2, true, false, true);
        Room room2 = new Room(0, 2, 106, 2, 2, false, false, true);

        repo.save(usr1);
        repoEmployee.save(emp1);
        repoEmployee.save(emp2);
        repoGuest.save(guest1);
        repoRoom.save(room1);
        repoRoom.save(room2);
    }
}
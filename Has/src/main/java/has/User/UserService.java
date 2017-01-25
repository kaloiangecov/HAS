package has.User;

import has.Employee.Employee;
import has.Employee.EmployeeRepository;
import has.Exceptions.UserAlreadyExists;
import has.Guest.Guest;
import has.Guest.GuestRepository;
import has.PersonalData.PersonalData;
import has.Room.Room;
import has.Room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private RoleRepository repoRole;

    @Autowired
    private EmployeeRepository repoEmployee;

    @Autowired
    private GuestRepository repoGuest;

    @Autowired
    private RoomRepository repoRoom;

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

        dbUser.setLastLogin(user.getLastLogin());
        dbUser.setPassword(user.getPassword());
        dbUser.setRegDate(user.getRegDate());
        dbUser.setUsername(user.getUsername());

        return repo.save(dbUser);
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public Page<User> searchUsers(int draw, int start, int length, String username, String email, Integer role) {

        PageRequest request = new PageRequest((start / length), length, Sort.Direction.ASC, "id");

        return repo.findByUsernameContainingAndEmailContaining(username, email, request);
    }

    public User findById(Long id) throws Exception {
        User dbUser = repo.findOne(id);
        if (dbUser == null) {
            throw new Exception("There is no user with such ID");
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

    public List<UserRole> getAllRoles() {
        return repoRole.findAll();
    }

    public UserRole findRoleById(Long id) {
        return repoRole.findOne(id);
    }

    @PostConstruct
    private void initSomeData() {

        RolePermission perm1 = new RolePermission("ADMIN");
        List<RolePermission> permissions = new ArrayList<>();
        List<RolePermission> permissions2 = new ArrayList<>();
        permissions.add(perm1);
        permissions2.add(new RolePermission("SHIT"));
        UserRole role1 = new UserRole("adm", permissions);

        User usr1 = new User("2017-01-10T12:30:00", "password", "shit@abv.bg", "2016-11-12T11:30:30", "ivan", role1);
        User usr2 = new User("2017-01-11T12:30:00", "123456789", "dick@abv.bg", "2016-09-12T11:30:30", "grigor", new UserRole("adm2", permissions2));

        repo.save(usr1);
        repo.save(usr2);

        Employee emp1 = new Employee(
                "12/11/2016",
                usr1,
                new PersonalData(
                        "some address",
                        "9203318220",
                        "Стефан Неделчев",
                        "10/12/2020",
                        "10/12/2010",
                        "МВР Търговище",
                        "123456123",
                        "0883504497"));

        Employee emp2 = new Employee(
                "25/08/2016",
                usr2,
                new PersonalData(
                        "somewhere",
                        "8888881122",
                        "Валери Божинов",
                        "10/07/2021",
                        "10/07/2011",
                        "МВР Някъде си",
                        "000777111",
                        "0891345090"));
        Employee emp3 = new Employee(
                "11/01/2017",
                usr2,
                new PersonalData(
                        "somewhere else 67",
                        "0088881122",
                        "Христо Стоичков",
                        "22/11/2022",
                        "22/11/2012",
                        "МВР София",
                        "123717111",
                        "0885678123"));

        repoEmployee.save(emp1);
        repoEmployee.save(emp2);
        repoEmployee.save(emp3);

        Guest guest1 = new Guest(
                0,
                usr2,
                new PersonalData(
                        "some address 2",
                        "9412567890",
                        "Христина Христова",
                        "11/12/2020",
                        "11/12/2010",
                        "МВР Варна",
                        "888000555",
                        "4543212453"));

        Guest guest2 = new Guest(
                0,
                usr1,
                new PersonalData(
                        "some address 3",
                        "9999999999",
                        "Шошо Мошо",
                        "06/07/2009",
                        "06/07/2019",
                        "МВР Варна",
                        "123123456",
                        "0875999999"));

        repoGuest.save(guest1);
        repoGuest.save(guest2);

        //Room room1 = new Room(1, 1, 104, 2, 2, true, false, true);
        //Room room2 = new Room(0, 2, 106, 2, 2, false, false, true);

        Boolean _children = true;
        Boolean _pets = false;
        Boolean _minibar = true;
        int roomStart = 100;

        for (int i = 0; i < 50; i++) {
            Room tempRoom = new Room(i % 2, i % 2, roomStart + i, i % 2, i % 2, _children, _pets, _minibar);
            repoRoom.save(tempRoom);
            _children = !_children;
            _pets = !_pets;
            _minibar = !_minibar;
            if (roomStart == i * 10)
                roomStart += (i * 10);
        }

        //repoRoom.save(room1);
        //repoRoom.save(room2);
    }
}
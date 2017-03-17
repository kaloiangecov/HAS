package has.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kaloi on 12/17/2016.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    @Query("select u from has.User.User u where u.enabled = true and u not in (select e.user from has.Employee.Employee e) and u not in (select g.user from has.Guest.Guest g) and u.userRole.id < 5")
    List<User> findFreeEmployeeUsers();

    @Query("select u from has.User.User u where u.enabled = true and u not in (select e.user from has.Employee.Employee e) and u not in (select g.user from has.Guest.Guest g) and u.userRole.id = 5")
    List<User> findFreeGuestUsers();

    Page<User> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUserRoleId(String username, String email, Long roleID, Pageable pageRequest);

    Page<User> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCase(String username, String email, Pageable pageRequest);
}

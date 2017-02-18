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

    User findByUsernameAndPassword(String username, String Password);

    @Query(value = "select * from T_USER where T_USER.USER_ID NOT IN (select E.USER_ID from EMPLOYEE E, T_USER U2 where U2.USER_ID = E.USER_ID)", nativeQuery = true)
    List<User> findFreeUsers();

    List<User> findByUsernameContainingAndEmailContaining(String username, String email);

    Page<User> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUserRoleId(String username, String email, Long roleID, Pageable pageRequest);
}

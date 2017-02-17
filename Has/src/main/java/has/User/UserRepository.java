package has.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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

    List<User> findByUsernameContainingAndEmailContaining(String username, String email);

    Page<User> findByUsernameContainingIgnoreCaseAndEmailContainingIgnoreCaseAndUserRoleId(String username, String email, Long roleID, Pageable pageRequest);
}

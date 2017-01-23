package has.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Created by kaloi on 12/17/2016.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    List<User> findByUsernameContainingAndEmailContaining(String username, String email);

    @Query("select r from UserRole r")
    List<UserRole> getAllRoles();

    @Query("select r from UserRole r where r.id = :id")
    UserRole getRoleById(@Param("id") Long id);
}

package has.Roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 1/23/2017.
 */
@Repository
public interface RoleRepository extends JpaRepository <UserRole, Long> {

    UserRole findByRoleName(String roleName);
}

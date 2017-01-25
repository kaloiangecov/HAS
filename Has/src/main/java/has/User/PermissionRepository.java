package has.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kaloi on 1/23/2017.
 */
@Repository
public interface PermissionRepository extends JpaRepository<RolePermission, Long> {
}

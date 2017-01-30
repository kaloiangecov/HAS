package has.Roles;

import has.Exceptions.RoleNameAlreadyExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kaloi on 1/30/2017.
 */
@Service
public class RoleService {

    @Autowired
    private RoleRepository repo;

    public UserRole findById(Long id) throws Exception {
        UserRole dbRole = repo.findOne(id);
        if (dbRole == null) {
            throw new Exception("There is no role with such ID");
        }
        return repo.findOne(id);
    }

    public List<UserRole> findAll() {
        return repo.findAll();
    }

    public UserRole save(UserRole userRole) throws RoleNameAlreadyExists {
        if (repo.findByRoleName(userRole.getRoleName()) != null) {
            throw new RoleNameAlreadyExists(userRole.getRoleName());
        }
        return repo.save(userRole);
    }

    public UserRole update(UserRole userRole, Long id) throws Exception {
        UserRole dbRole = repo.findOne(id);
        if (dbRole == null) {
            throw new Exception("There is no role with such ID");
        }

        dbRole.setPermissions(userRole.getPermissions());
        dbRole.setRoleName(userRole.getRoleName());
        return repo.save(dbRole);
    }

    public UserRole remove(Long id) throws Exception {
        UserRole dbRole = repo.findOne(id);
        if (dbRole == null) {
            throw new Exception("There is no role with such ID");
        }

        repo.delete(dbRole);
        return dbRole;
    }
}

package has.Exceptions;

/**
 * Created by kaloi on 1/30/2017.
 */
public class RoleNameAlreadyExists extends MasterException {

    public RoleNameAlreadyExists(String roleName) {
        super("There is already a role with role name: " + roleName + " in the system.");
    }
}

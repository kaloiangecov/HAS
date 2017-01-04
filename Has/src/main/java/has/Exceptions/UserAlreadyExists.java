package has.Exceptions;

/**
 * Created by kaloi on 1/2/2017.
 */
public class UserAlreadyExists extends MasterException {

    public UserAlreadyExists(String username) {
        super("There is already an user with username: " + username + " in the system.");
    }
}

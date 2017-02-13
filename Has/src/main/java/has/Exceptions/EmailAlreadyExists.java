package has.Exceptions;

/**
 * Created by kaloi on 2/13/2017.
 */
public class EmailAlreadyExists extends MasterException {

    public EmailAlreadyExists(String email) {
        super("There is already an user with email: " + email + " in the system.");
    }
}

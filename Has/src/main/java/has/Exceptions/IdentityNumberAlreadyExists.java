package has.Exceptions;

/**
 * Created by kaloi on 1/23/2017.
 */
public class IdentityNumberAlreadyExists extends MasterException {

    public IdentityNumberAlreadyExists(String egn) {
        super("There is already a person with identity number: " + egn + " in the system.");
    }
}
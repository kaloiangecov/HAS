package has.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kaloi on 2/20/2017.
 */
public class Validator {

    public static boolean isValidIssueDate(String issueDate, String expirationDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date issue = format.parse(issueDate);
        Date expiration = format.parse(expirationDate);
        if (issue.after(expiration)) {
            return false;
        }
        if (issue.getTime() > new Date().getTime()) {
            return false;
        }
        return true;
    }
}

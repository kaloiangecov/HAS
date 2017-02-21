package has.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kaloi on 2/20/2017.
 */
public class Validator {

    /**
     * This method validates if the issue date is before the expiration date
     * it doesn't work if the issue date is ahead of the present day and time
     *
     * @param issueDate
     * @param expirationDate
     * @return
     * @throws ParseException
     */
    public static boolean isValidIssueExpireDate(String issueDate, String expirationDate) throws ParseException {
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

    /**
     * This method validates if the start date is before the end date
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static boolean isValidStartEndDate(String startDate, String endDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = format.parse(startDate);
        Date end = format.parse(endDate);
        if (start.after(end)) {
            return false;
        }
        return true;
    }
}

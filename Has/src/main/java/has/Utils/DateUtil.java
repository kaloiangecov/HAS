package has.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date comparison utility.
 * Created by Chokleet on 19.2.2017 г..
 */
public class DateUtil {

    private DateUtil(){
    }

    public static boolean isValid(String issueDate, String expirationDate) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date issue = format.parse(issueDate);
        if(issue.getTime() > new Date().getTime()){
            return false;
        }
        Date expiration = format.parse(expirationDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(issue);
        cal.add(Calendar.YEAR, 10);
        issue = cal.getTime();
        if(!issue.equals(expiration)){
            throw new Exception("Expiration date must be 10 years after issue date");
        }
        return true;
    }
}

package has.Utils;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kaloi on 3/19/2017.
 */
@Component
public class TimeFormatter {

    public static LocalTime test(String time) {
        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("h:mm").toFormatter();
        LocalTime parsedTime = LocalTime.parse(time, parseFormat);
        return parsedTime;
    }

    public static String hoursAndMinutes(LocalTime time) {
        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("hh:mm").toFormatter();
        String timeToParse = parseFormat.print(time);
        return timeToParse;
    }

    public static String getAsYearMonthDayFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getNewDateAsFullString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        return date;
    }

    public static String getTomorrowDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        return getAsYearMonthDayFormat(dt);
    }

}

package has.Utils;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * Created by kaloi on 3/19/2017.
 */
public class TimeFormatter {

    public static LocalTime timeParse(String time) {
        DateTimeFormatter parseFormat = new DateTimeFormatterBuilder().appendPattern("h:mm").toFormatter();
        LocalTime parsedTime = LocalTime.parse(time, parseFormat);
        return parsedTime;
    }
}

package cz.osu.theatre.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTimeFormatter {

    public static String changeDateFormat(String date, String old_format, String new_format) {
        try {
            SimpleDateFormat oldDateFormat = new SimpleDateFormat(old_format);
            Date parsedDate = oldDateFormat.parse(date);
            oldDateFormat.applyPattern(new_format);

            return oldDateFormat.format(parsedDate);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Occurred during parsing String date: %s ", date));
        }
    }


}

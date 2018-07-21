package me.franciscoigor.habits.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String WEEKDAY_MONDAY = "Monday";
    public static final String WEEKDAY_TUESDAY = "Tuesday";
    public static final String WEEKDAY_WEDNESDAY = "Wednesday";
    public static final String WEEKDAY_THURSDAY = "Thursday";
    public static final String WEEKDAY_FRIDAY = "Friday";
    public static final String WEEKDAY_SATURDAY = "Saturday";
    public static final String WEEKDAY_SUNDAY = "Sunday";

    public static String[] WEEKDAYS ;

    static SimpleDateFormat dateFormat;

    static{
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String[] days = {
                DateUtils.WEEKDAY_SUNDAY,
                DateUtils.WEEKDAY_MONDAY ,
                DateUtils.WEEKDAY_TUESDAY,
                DateUtils.WEEKDAY_WEDNESDAY,
                DateUtils.WEEKDAY_THURSDAY,
                DateUtils.WEEKDAY_FRIDAY,
                DateUtils.WEEKDAY_SATURDAY
        };
        WEEKDAYS = days;
    }

    public static String format(Date date){
        return dateFormat.format(date);
    }

    public static Date parse(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    public static Date today(){
        Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());
        return Calendar.getInstance().getTime();
    }

    public static String weekDay(Date date){
        Calendar.getInstance().setTime(date);
        int day=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return WEEKDAYS[day-1];
    }
}

package me.franciscoigor.habits.base;

import android.app.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String WEEKDAY_MONDAY = "monday";
    public static final String WEEKDAY_TUESDAY = "tuesday";
    public static final String WEEKDAY_WEDNESDAY = "wednesday";
    public static final String WEEKDAY_THURSDAY = "thursday";
    public static final String WEEKDAY_FRIDAY = "friday";
    public static final String WEEKDAY_SATURDAY = "saturday";
    public static final String WEEKDAY_SUNDAY = "sunday";

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

    public static Date yesterday(){
        Calendar.getInstance().setTimeInMillis(System.currentTimeMillis());

        return new Date(Calendar.getInstance().getTimeInMillis()-86400000l);
    }

    public static String weekDay(Date date){
        Calendar.getInstance().setTime(date);
        int day=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return WEEKDAYS[day-1];
    }

    public static String getWeekDayName(Activity activity, String weekday){
        if (weekday==null) return null;
        int id = activity.getResources().getIdentifier(weekday, "string", activity.getPackageName());
        return activity.getString(id);
    }

    public static String[] getWeekDays(Activity activity){
        String[] result=new String[WEEKDAYS.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = getWeekDayName(activity, WEEKDAYS[i]);
        }
        return result;
    }


}

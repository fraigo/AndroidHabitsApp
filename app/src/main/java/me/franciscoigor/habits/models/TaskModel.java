package me.franciscoigor.habits.models;


import me.franciscoigor.habits.base.DataModel;

public class TaskModel extends DataModel {

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_SUBCATEGORY = "subcategory";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_ENABLED = "enabled";

    public static final String CATEGORY_DAILY = "Daily";
    public static final String CATEGORY_WEEKLY = "Weekly";
    public static final String CATEGORY_MONTHLY = "Monthly";
    public static final String CATEGORY_ONETIME = "One time";

    public static final String WEEKDAY_MONDAY = "Monday";
    public static final String WEEKDAY_TUESDAY = "Tuesday";
    public static final String WEEKDAY_WEDNESDAY = "Wednesday";
    public static final String WEEKDAY_THURSDAY = "Thursday";
    public static final String WEEKDAY_FRIDAY = "Friday";
    public static final String WEEKDAY_SATURDAY = "Saturday";
    public static final String WEEKDAY_SUNDAY = "Sunday";

    public static String[] WEEKDAYS ;
    public static String[] CATEGORIES ;

    static {
        String[] days = {
                WEEKDAY_MONDAY ,
                WEEKDAY_TUESDAY,
                WEEKDAY_WEDNESDAY,
                WEEKDAY_THURSDAY,
                WEEKDAY_FRIDAY,
                WEEKDAY_SATURDAY,
                WEEKDAY_SUNDAY
        };
        WEEKDAYS = days;

        String[] categories = {
                CATEGORY_DAILY,
                CATEGORY_WEEKLY,
                CATEGORY_MONTHLY,
                CATEGORY_ONETIME
        };
        CATEGORIES = categories;
    }


    public TaskModel(){
        this(null, null, null, null, "08:00", false);
    }

    public TaskModel(String title, String description, String category, String subcategory, String time, boolean finished){
        super("tasks");
        addField(FIELD_TITLE);
        addField(FIELD_DESCRIPTION);
        addField(FIELD_CATEGORY);
        addField(FIELD_SUBCATEGORY);
        addField(FIELD_ENABLED);
        addField(FIELD_TIME);

        setValue(FIELD_TITLE, title);
        setValue(FIELD_DESCRIPTION, description);
        setValue(FIELD_CATEGORY, category);
        setValue(FIELD_SUBCATEGORY, subcategory);
        setValue(FIELD_ENABLED, finished);
        setValue(FIELD_TIME, time);

    }
}

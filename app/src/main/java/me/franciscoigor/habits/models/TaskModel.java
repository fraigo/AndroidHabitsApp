package me.franciscoigor.habits.models;


import java.util.ArrayList;

import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;

public class TaskModel extends DataModel {

    public static final String TABLE_NAME = "tasks";

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


    public static String[] CATEGORIES ;

    static {


        String[] categories = {
                CATEGORY_DAILY,
                CATEGORY_WEEKLY,
                CATEGORY_MONTHLY,
                CATEGORY_ONETIME
        };
        CATEGORIES = categories;
    }


    public TaskModel(){
        this(null, null, CATEGORY_DAILY, null, "08:00", true);
    }

    public TaskModel(String title, String description, String category, String subcategory, String time, boolean enabled){
        super(TABLE_NAME);
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
        setValue(FIELD_ENABLED, enabled);
        setValue(FIELD_TIME, time);

    }


}

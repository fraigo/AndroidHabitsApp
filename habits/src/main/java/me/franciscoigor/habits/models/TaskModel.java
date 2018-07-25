package me.franciscoigor.habits.models;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;

import me.franciscoigor.habits.R;
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

    public static final String CATEGORY_DAILY = "daily";
    public static final String CATEGORY_WEEKLY = "weekly";
    public static final String CATEGORY_MONTHLY = "monthly";
    public static final String CATEGORY_WEEKDAYS = "weekdays";
    public static final String CATEGORY_WEEKENDS = "weekends";
    public static final String CATEGORY_ONETIME = "one_time";


    public static String[] CATEGORIES ;
    public static String[] COLORS ;

    static {


        String[] categories = {
                CATEGORY_DAILY,
                CATEGORY_WEEKLY,
                CATEGORY_MONTHLY,
                CATEGORY_WEEKDAYS,
                CATEGORY_WEEKENDS,
                CATEGORY_ONETIME
        };
        String[] colors = {
                "#000080",
                "#008000",
                "#008080",
                "#FF5733",
                "#800080",
                "#808080"
        };
        CATEGORIES = categories;
        COLORS = colors;
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

    public static String[] getCateogries(Activity activity){
        String[] result=new String[CATEGORIES.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = getCategory(activity, CATEGORIES[i]);
        }
        return result;
    }

    public static String getCategory(Activity activity, String category){
        if (category==null) return null;
        int id = activity.getResources().getIdentifier(category, "string", activity.getPackageName());
        return activity.getString(id);
    }


    public static int getColor(DataModel model) {
        String category = model.getStringValue(FIELD_CATEGORY);
        int pos= Arrays.asList(CATEGORIES).indexOf(category);
        return Color.parseColor(COLORS[pos]);
    }
}

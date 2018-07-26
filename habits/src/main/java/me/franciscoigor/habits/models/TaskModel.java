package me.franciscoigor.habits.models;


import android.app.Activity;
import android.graphics.Color;

import java.util.Arrays;

import me.franciscoigor.habits.base.DataModel;

public class TaskModel extends DataModel {

    public static final String TABLE_NAME = "tasks";

    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_FREQUENCY = "frequency";
    public static final String FIELD_SUBCATEGORY = "subcategory";
    public static final String FIELD_TIME = "time";
    public static final String FIELD_ENABLED = "enabled";

    public static final String FREQUENCY_DAILY = "daily";
    public static final String FREQUENCY_WEEKLY = "weekly";
    public static final String FREQUENCY_MONTHLY = "monthly";
    public static final String FREQUENCY_WEEKDAYS = "weekdays";
    public static final String FREQUENCY_WEEKENDS = "weekends";
    public static final String FREQUENCY_ONETIME = "one_time";


    public static String[] FREQUENCIES;
    public static String[] COLORS ;

    static {


        String[] categories = {
                FREQUENCY_DAILY,
                FREQUENCY_WEEKLY,
                FREQUENCY_MONTHLY,
                FREQUENCY_WEEKDAYS,
                FREQUENCY_WEEKENDS,
                FREQUENCY_ONETIME
        };
        String[] colors = {
                "#000080",
                "#008000",
                "#008080",
                "#FF8040",
                "#800080",
                "#808080"
        };
        FREQUENCIES = categories;
        COLORS = colors;
    }


    public TaskModel(){
        this(null, null, FREQUENCY_DAILY, null, "08:00", true);
    }

    public TaskModel(String title, String description, String category, String subcategory, String time, boolean enabled){
        super(TABLE_NAME);
        addField(FIELD_TITLE);
        addField(FIELD_DESCRIPTION);
        addField(FIELD_FREQUENCY);
        addField(FIELD_SUBCATEGORY);
        addField(FIELD_ENABLED);
        addField(FIELD_TIME);

        setValue(FIELD_TITLE, title);
        setValue(FIELD_DESCRIPTION, description);
        setValue(FIELD_FREQUENCY, category);
        setValue(FIELD_SUBCATEGORY, subcategory);
        setValue(FIELD_ENABLED, enabled);
        setValue(FIELD_TIME, time);

    }

    public static String[] getFrequencyList(Activity activity){
        String[] result=new String[FREQUENCIES.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = getFrequency(activity, FREQUENCIES[i]);
        }
        return result;
    }

    public static String getFrequency(Activity activity, String frequency){
        if (frequency==null) return null;
        int id = activity.getResources().getIdentifier(frequency, "string", activity.getPackageName());
        return activity.getString(id);
    }


    public static int getColor(DataModel model) {
        String category = model.getStringValue(FIELD_FREQUENCY);
        int pos= Arrays.asList(FREQUENCIES).indexOf(category);
        return Color.parseColor(COLORS[pos]);
    }
}

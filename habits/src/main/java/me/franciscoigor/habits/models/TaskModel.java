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
    public static final String FIELD_FREQ_DETAIL = "freq_detail" ;
    public static final String FIELD_TIME = "time";
    public static final String FIELD_ENABLED = "enabled";
    public static final String FIELD_CATEGORY = "category";

    public static final String FREQUENCY_DAILY = "daily";
    public static final String FREQUENCY_WEEKLY = "weekly";
    public static final String FREQUENCY_MONTHLY = "monthly";
    public static final String FREQUENCY_WEEKDAYS = "weekdays";
    public static final String FREQUENCY_WEEKENDS = "weekends";
    public static final String FREQUENCY_ONETIME = "one_time";

    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_STUDY = "study";
    public static final String CATEGORY_FINANCIAL = "financial";
    public static final String CATEGORY_WORK = "work";
    public static final String CATEGORY_OTHER = "other";
    public static final String CATEGORY_HOME = "home";
    public static final String CATEGORY_ENTERTAINMENT = "entertainment";

    public static String[] FREQUENCIES;
    public static String[] COLORS ;
    public static String[] CATEGORIES ;

    static {


        String[] frequencies = {
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
        String[] categories={
                CATEGORY_HEALTH,
                CATEGORY_WORK,
                CATEGORY_STUDY,
                CATEGORY_FINANCIAL,
                CATEGORY_HOME,
                CATEGORY_ENTERTAINMENT,
                CATEGORY_OTHER
        };
        FREQUENCIES = frequencies;
        COLORS = colors;
        CATEGORIES = categories;
    }


    public TaskModel(){
        this(null, null, FREQUENCY_DAILY, null, CATEGORY_OTHER, "08:00", true);
    }

    public TaskModel(String title, String description, String frequency, String frequencyDetail, String category, String time, boolean enabled){
        super(TABLE_NAME);
        addField(FIELD_TITLE);
        addField(FIELD_DESCRIPTION);
        addField(FIELD_FREQUENCY);
        addField(FIELD_FREQ_DETAIL);
        addField(FIELD_ENABLED);
        addField(FIELD_TIME);
        addField(FIELD_CATEGORY);


        setValue(FIELD_TITLE, title);
        setValue(FIELD_DESCRIPTION, description);
        setValue(FIELD_FREQUENCY, frequency);
        setValue(FIELD_FREQ_DETAIL, frequencyDetail);
        setValue(FIELD_ENABLED, enabled);
        setValue(FIELD_TIME, time);
        setValue(FIELD_CATEGORY, category);

    }

    public static String[] getFrequencyList(Activity activity){
        String[] result=new String[FREQUENCIES.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = getString(activity, FREQUENCIES[i]);
        }
        return result;
    }

    public static String[] getCategoryNames(Activity activity){
        String[] result=new String[CATEGORIES.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = getString(activity, CATEGORIES[i]);
        }
        return result;
    }

    public static String getCategoryName(Activity activity, String category){
        return getString(activity, category);
    }

    public static String getString(Activity activity, String stringId){
        if (stringId==null) return null;
        int id = activity.getResources().getIdentifier(stringId, "string", activity.getPackageName());
        return activity.getString(id);
    }


    public static int getColor(DataModel model) {
        String frequency = model.getStringValue(FIELD_FREQUENCY);
        int pos= Arrays.asList(FREQUENCIES).indexOf(frequency);
        return Color.parseColor(COLORS[pos]);
    }
}

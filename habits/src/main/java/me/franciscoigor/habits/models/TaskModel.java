package me.franciscoigor.habits.models;


import android.app.Activity;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;

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
                "#80C020",
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
        String frequency = model.getStringValue(FIELD_CATEGORY);
        int pos= Arrays.asList(CATEGORIES).indexOf(frequency);
        return Color.parseColor(COLORS[pos]);
    }

    public static ArrayList<DataModel> currentTasks(Date current) {
        String currentDate = DateUtils.format(current);
        String currentWeekDay = DateUtils.weekDay(current);
        String todayDate = DateUtils.format(DateUtils.today());

        System.out.println("CURRENT DATE "+ currentDate+ " "+ currentWeekDay);

        String[] columns = { "tasks.*"};
        ArrayList<DataModel> filtered= DatabaseHelper.getItems(
                String.format("tasks left join taskActions ON taskActions.task_id=tasks._id and taskActions.task_date = '%s'", currentDate),
                columns,
                String.format("taskActions.task_id is null and tasks.enabled = '1' and ( ( tasks.%s = '%s' ) OR ( tasks.%s = '%s' and  tasks.%s = '%s') OR ( tasks.%s = '%s' and  '%s' not in ('saturday','sunday') ) OR ( tasks.%s = '%s' and  '%s' in ('saturday','sunday') ))",
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_DAILY,
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_WEEKLY,
                        TaskModel.FIELD_FREQ_DETAIL,
                        currentWeekDay,
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_WEEKDAYS,
                        currentWeekDay,
                        TaskModel.FIELD_FREQUENCY,
                        TaskModel.FREQUENCY_WEEKENDS,
                        currentWeekDay
                ),
                null);
        return filtered;
    }
}

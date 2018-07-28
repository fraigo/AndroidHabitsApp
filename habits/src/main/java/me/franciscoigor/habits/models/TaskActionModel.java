package me.franciscoigor.habits.models;


import java.util.ArrayList;
import java.util.Date;

import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.base.ListFragment;

public class TaskActionModel extends DataModel {

    public static final String TABLE_NAME = "taskActions";

    public static final String FIELD_TASK = "task_id";
    public static final String FIELD_TIME_MINUTES = "time_minutes";
    public static final String FIELD_FINISHED = "finished";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DATE = "task_date";
    public static final String FIELD_TIME = "task_time";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_FREQUENCY = "frequency";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_TIME_START = "time_start";
    public static final String FIELD_TIME_END = "time_end";

    static {

    }


    public TaskActionModel(){
        this(0, "", DateUtils.today(), "08:00", TaskModel.FREQUENCY_DAILY, TaskModel.CATEGORY_OTHER , 0,  false, false);
    }

    public TaskActionModel(int taskId, String title, Date date, String time, String frequency, String category, int timeMinutes, boolean finished, boolean deleted){
        super(TABLE_NAME);
        addField(FIELD_TASK);
        addField(FIELD_TIME_MINUTES);
        addField(FIELD_FINISHED);
        addField(FIELD_TITLE);
        addField(FIELD_DATE);
        addField(FIELD_TIME);
        addField(FIELD_FREQUENCY);
        addField(FIELD_CATEGORY);
        addField(FIELD_DELETED);

        setValue(FIELD_TASK, taskId);
        setValue(FIELD_TITLE, title);
        setValue(FIELD_DATE, date);
        setValue(FIELD_TIME, time);
        setValue(FIELD_FREQUENCY, frequency);
        setValue(FIELD_CATEGORY, category);
        setValue(FIELD_TIME_MINUTES, timeMinutes);
        setValue(FIELD_FINISHED, finished);
        setValue(FIELD_DELETED, deleted);


    }

    public static DataModel createItem(DataModel item, Date current) {
        return new TaskActionModel(
                item.getIntValue("_id"),
                item.getStringValue(TaskModel.FIELD_TITLE),
                current,
                item.getStringValue(TaskModel.FIELD_TIME),
                item.getStringValue(TaskModel.FIELD_FREQUENCY),
                item.getStringValue(TaskModel.FIELD_CATEGORY),
                0,
                false,
                false
        );
    }


    public static void createTaskActions(Date current, ListFragment.ItemAdapter adapter){
        ArrayList<DataModel> filtered = TaskModel.currentTasks(current);
        System.out.println(String.format("Creating %d new tasks for %s", filtered.size(), DateUtils.format(current)));
        for (DataModel item: filtered) {
            if (adapter != null) {
                adapter.addItem(createItem(item, current));
            }else{
                DatabaseHelper.insert(createItem(item, current));
            }

        }
    }
}

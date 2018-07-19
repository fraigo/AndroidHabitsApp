package me.franciscoigor.habits.models;


import me.franciscoigor.habits.base.DataModel;

public class TaskActionModel extends DataModel {

    public static final String FIELD_TASK = "task_id";
    public static final String FIELD_TIME_START = "time_start";
    public static final String FIELD_TIME_END = "time_end";
    public static final String FIELD_TIME_MINUTES = "time_minutes";
    public static final String FIELD_FINISHED = "finished";
    public static final String FIELD_TITLE = "title";

    public static String[] WEEKDAYS ;
    public static String[] CATEGORIES ;

    static {

    }


    public TaskActionModel(){
        this(0, "", 0, false);
    }

    public TaskActionModel(int taskId, String title, int timeMinutes, boolean finished){
        super("taskActions");
        addField(FIELD_TASK);
        addField(FIELD_TIME_MINUTES);
        addField(FIELD_FINISHED);
        addField(FIELD_TITLE);

        setValue(FIELD_TASK, taskId);
        setValue(FIELD_TITLE, title);
        setValue(FIELD_TIME_MINUTES, timeMinutes);
        setValue(FIELD_FINISHED, finished);


    }
}

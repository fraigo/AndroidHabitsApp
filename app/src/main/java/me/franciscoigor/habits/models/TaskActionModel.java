package me.franciscoigor.habits.models;


import java.util.Calendar;
import java.util.Date;

import me.franciscoigor.habits.base.DataModel;

public class TaskActionModel extends DataModel {

    public static final String FIELD_TASK = "task_id";
    public static final String FIELD_TIME_MINUTES = "time_minutes";
    public static final String FIELD_FINISHED = "finished";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_DATE = "task_date";
    public static final String FIELD_DELETED = "deleted";
    public static final String FIELD_TIME_START = "time_start";
    public static final String FIELD_TIME_END = "time_end";

    public static String[] WEEKDAYS ;
    public static String[] CATEGORIES ;

    static {

    }


    public TaskActionModel(){
        this(0, "", Calendar.getInstance().getTime(), 0, false, false);
    }

    public TaskActionModel(int taskId, String title, Date date, int timeMinutes, boolean finished, boolean deleted){
        super("taskActions");
        addField(FIELD_TASK);
        addField(FIELD_TIME_MINUTES);
        addField(FIELD_FINISHED);
        addField(FIELD_TITLE);
        addField(FIELD_DATE);
        addField(FIELD_DELETED);


        setValue(FIELD_TASK, taskId);
        setValue(FIELD_TITLE, title);
        setValue(FIELD_DATE, date);
        setValue(FIELD_TIME_MINUTES, timeMinutes);
        setValue(FIELD_FINISHED, finished);
        setValue(FIELD_DELETED, deleted);


    }
}

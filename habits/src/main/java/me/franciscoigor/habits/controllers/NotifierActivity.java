package me.franciscoigor.habits.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.base.NotificationHelper;
import me.franciscoigor.habits.base.Storage;
import me.franciscoigor.habits.models.OptionsModel;
import me.franciscoigor.habits.models.TaskActionModel;
import me.franciscoigor.habits.models.TaskModel;


public class NotifierActivity extends BroadcastReceiver {

    static PendingIntent appIntent;
    public static final int NOTIFICATION_ID = 65432;
    public static final int NOTIFICATION_FREQ = 600;

    public static void startNotifier(Context context){

        Calendar calendar = Calendar.getInstance();
        Long time = new GregorianCalendar().getTimeInMillis()+60*06*24*1000;
        Intent intentAlarm = new Intent(context, NotifierActivity.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent=PendingIntent.getBroadcast(context, 1, intentAlarm, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time, alarmIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),1000 * NOTIFICATION_FREQ, alarmIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String currentDate= DateUtils.format(DateUtils.today());
        System.out.println(DatabaseHelper.getDatabase(context).getAttachedDbs());

        String dateCondition = String.format("%s = '%s' AND %s = '%d' AND  %s = '%d' ", TaskActionModel.FIELD_DATE,currentDate, TaskActionModel.FIELD_FINISHED , 0, TaskActionModel.FIELD_DELETED, 0);
        ArrayList<DataModel> filtered= DatabaseHelper.getItems(TaskActionModel.TABLE_NAME, null, dateCondition ,null);
        System.out.println("Tasks:" + filtered);
        if (filtered.size()>0) {
            String message = String.format("%d pending task(s) for today", filtered.size());
            String names= "";
            for (DataModel model: filtered
                 ) {
                names+= "\n" + (model.getStringValue(TaskActionModel.FIELD_TITLE) + " @ " + model.getStringValue(TaskActionModel.FIELD_TIME));
            }
            System.out.println(message);
            NotificationHelper helper = new NotificationHelper(context, MainActivity.class);
            helper.notify( NOTIFICATION_ID,context.getString(R.string.app_name), message + names);

        }

    }

    public static void notifyUser(Context context, String title, String message){
        if (appIntent == null){
            Intent intent = new Intent(context, MainActivity.class);
            appIntent = PendingIntent.getActivity(context, 0, intent, 0);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "MAIN")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(appIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Storage.addInt("NOTIFICATION_ID",1);
        int id = Storage.getInt("NOTIFICATION_ID",1);
        notificationManager.notify(id, mBuilder.build());
    }



}

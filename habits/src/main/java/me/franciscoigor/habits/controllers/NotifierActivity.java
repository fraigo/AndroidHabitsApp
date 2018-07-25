package me.franciscoigor.habits.controllers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import me.franciscoigor.habits.R;
import me.franciscoigor.habits.base.DataModel;
import me.franciscoigor.habits.base.DatabaseHelper;
import me.franciscoigor.habits.base.DateUtils;
import me.franciscoigor.habits.base.Storage;
import me.franciscoigor.habits.models.TaskActionModel;
import me.franciscoigor.habits.models.TaskModel;


public class NotifierActivity extends AppCompatActivity {

    static PendingIntent appIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String currentDate= DateUtils.weekDay(DateUtils.today());
        System.out.println("START notifier");
        ArrayList<DataModel> filtered= DatabaseHelper.getItems("tasksActions", null, String.format("%s <> '%s'", TaskActionModel.FIELD_DATE,currentDate),new String[0]);
        if (filtered.size()>0) {
            NotifierActivity.notifyUser(this, "Pending tasks", String.format("%d pending tasks for today", filtered.size()));
        }
    }

    public static void startNotifier(Context context){
        Intent intent = new Intent(context, NotifierActivity.class);
        PendingIntent notifierActivity = PendingIntent.getActivity(context, 0, intent, 0);
        AlarmManager alarmMgr;
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10000,
                60000, notifierActivity);
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

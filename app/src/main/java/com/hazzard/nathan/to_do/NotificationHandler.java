package com.hazzard.nathan.to_do;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationHandler {
    private NotificationManager manager;
    private AlarmManager alarmManager;
    private Context CONTEXT;

    public NotificationHandler(Context context) {
        CONTEXT = context;
        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    }

    public void taskNotification(Task task) {
        if(System.currentTimeMillis() < task.getDate().getTimeInMillis()) {
            Intent notificationHolder = new Intent(CONTEXT, com.hazzard.nathan.to_do.AlarmReceiver.class);
            notificationHolder.putExtra("Task", task);
            PendingIntent NotificationIntent = PendingIntent.getBroadcast(CONTEXT, task.getRequestCode(), notificationHolder, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(alarmManager.RTC_WAKEUP, task.getDate().getTimeInMillis(), NotificationIntent);
        }
    }

    public static void clearNotification(Context context, Task task) {
        if(System.currentTimeMillis() < task.getDate().getTimeInMillis()) {
            Intent notificationHolder = new Intent(context, com.hazzard.nathan.to_do.AlarmReceiver.class);
            notificationHolder.putExtra("Task", task);
            PendingIntent NotificationIntent = PendingIntent.getBroadcast(context, task.getRequestCode(), notificationHolder, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarmManager.cancel(NotificationIntent);
        }
    }
}
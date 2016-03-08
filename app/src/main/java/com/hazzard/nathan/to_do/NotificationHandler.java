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
            PendingIntent NotificationIntent = PendingIntent.getBroadcast(CONTEXT, task.getRequestCode(), notificationHolder, 0);
            alarmManager.set(alarmManager.RTC_WAKEUP, task.getDate().getTimeInMillis(), NotificationIntent);
        }
    }
}
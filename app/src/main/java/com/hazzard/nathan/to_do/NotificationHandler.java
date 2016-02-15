package com.hazzard.nathan.to_do;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

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
        Intent notificationHolder = new Intent(CONTEXT, com.hazzard.nathan.to_do.AlarmReceiver.class);
        notificationHolder.putExtra("Task", task);
        PendingIntent NotificationIntent = PendingIntent.getBroadcast(CONTEXT, 0, notificationHolder, 0);
        alarmManager.set(alarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, NotificationIntent);
    }
}
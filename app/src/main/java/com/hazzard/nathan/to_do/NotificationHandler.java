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
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private AlarmManager alarmManager;
    private Context CONTEXT;

    public NotificationHandler(Context context) {
        CONTEXT = context;
        builder = new NotificationCompat.Builder(context);
        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        builder.setVibrate(new long[] {0, 200});
    }

    public void taskNotification(Task task) {
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(task.getName());
        builder.setContentText(task.printDate(task.getDate()));

        Intent viewIntent = new Intent(CONTEXT, TaskCreator.class);
        viewIntent.putExtra("Task", task);
        PendingIntent pViewIntent = PendingIntent.getActivity(CONTEXT, 0, viewIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(R.mipmap.ic_launcher, "View", pViewIntent);
        Notification notification = builder.build();

        manager.notify(1, notification);
        //PendingIntent NotificationIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        //alarmManager.set(alarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pViewIntent);
    }
}
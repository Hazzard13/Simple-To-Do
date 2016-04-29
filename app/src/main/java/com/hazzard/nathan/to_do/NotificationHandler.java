package com.hazzard.nathan.to_do;

/**
 * NotificationHandler is a collection of methods containing everything Simple To-Do does with notifications
 * It's used to create, remove, and dismiss notifications
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class NotificationHandler {
    private NotificationManager manager;
    private AlarmManager alarmManager;
    private static Context CONTEXT;
    public NotificationHandler(Context context) {
        CONTEXT = context;
        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    }

    public void taskNotification(Task task) {
        ArrayList<GregorianCalendar> timeList = task.getTimeList();
        for (int i = 0; i < timeList.size(); i++) {
            if (System.currentTimeMillis() < timeList.get(i).getTimeInMillis()) {
                Intent notificationHolder = new Intent(CONTEXT, com.hazzard.nathan.to_do.AlarmReceiver.class);
                notificationHolder.putExtra("Task", task);
                notificationHolder.putExtra("requestCode", task.getRequestCodes().get(i));
                PendingIntent NotificationIntent = PendingIntent.getBroadcast(CONTEXT, task.getRequestCodes().get(i), notificationHolder, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(alarmManager.RTC_WAKEUP, timeList.get(i).getTimeInMillis(), NotificationIntent);
            }
        }
    }

    public static void clearNotification(Context context, Task task) {
        ArrayList<GregorianCalendar> timeList = task.getTimeList();
        for (int i = 0; i < timeList.size(); i++) {
            if (System.currentTimeMillis() < timeList.get(i).getTimeInMillis()) {
                Intent notificationHolder = new Intent(context, com.hazzard.nathan.to_do.AlarmReceiver.class);
                notificationHolder.putExtra("Task", task);
                notificationHolder.putExtra("requestCode", task.getRequestCodes().get(i));
                PendingIntent NotificationIntent = PendingIntent.getBroadcast(context, task.getRequestCodes().get(i), notificationHolder, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                alarmManager.cancel(NotificationIntent);
            }
        }
    }

    public static void dismissNotification(Context context, int requestCode) {
        ((NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE)).cancel(requestCode);
    }
}
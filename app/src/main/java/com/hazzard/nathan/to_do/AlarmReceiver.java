package com.hazzard.nathan.to_do;

/**
 * AlarmReceiver is a specialised BroadcastReceiver that responds to the android AlarmManager's Task broadcasts
 * It creates the customized task notification that will be displayed, and displays it to the user.
 * It's also responsible for repeating tasks at their due date
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Task task = (Task) intent.getSerializableExtra("Task");
        int requestCode = (int) intent.getSerializableExtra("requestCode");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setContentTitle(task.getName());
        builder.setContentText(DateFormatter.printDate(task.getTimeList().get(0)) + " at " + DateFormatter.printTime(task.getTimeList().get(0)));
        builder.setAutoCancel(true);

        builder.setSound(Uri.parse(settings.getString(Settings.RINGTONE_KEY, "default ringtone")));
        if(settings.getBoolean(Settings.VIBRATE_KEY, true)) {
            builder.setVibrate(new long[]{0, 200});
        }

        builder.setLights(ContextCompat.getColor(context, R.color.colorPrimary), 1000, 1000);


        //Sets the notification to open the task for viewing when tapped
        Intent viewIntent = new Intent(context, TaskEditor.class);
        viewIntent.putExtra("Task", task);
        viewIntent.putExtra("requestCode", requestCode);
        PendingIntent pViewIntent = PendingIntent.getActivity(context, requestCode, viewIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pViewIntent);

        //Creates the delete button on the notification
        Intent deleteIntent = new Intent(context, com.hazzard.nathan.to_do.DeleteReceiver.class);
        deleteIntent.putExtra("requestCode", requestCode);
        PendingIntent deleteViewIntent = PendingIntent.getBroadcast(context, requestCode, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_delete_white_24dp, "Delete", deleteViewIntent);

        Notification notification = builder.build();
        manager.notify(requestCode, notification);

        //Updates the task if it's set to repeat
        if (task.repeats()) {
            task.repeat();
            ArrayList<Task> taskList = TaskListManager.loadTaskList(context);
            for (int i = 0; i < taskList.size(); i++) {
                if(taskList.get(i).getRequestCodes().get(0).equals(task.getRequestCodes().get(0))) {
                    NotificationHandler.clearNotification(context, taskList.get(i));
                    taskList.remove(i);
                }
            }
            taskList.add(task);
            (new NotificationHandler(context)).taskNotification(task);
            TaskListManager.saveTaskList(context, taskList);
        }
    }
}

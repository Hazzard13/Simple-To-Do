package com.hazzard.nathan.to_do;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Task task = (Task) intent.getSerializableExtra("Task");
        int requestCode = (int) intent.getSerializableExtra("requestCode");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context);
        builder.setSound(Uri.parse(settings.getString(Settings.RINGTONE_KEY, "default ringtone")));
        if(settings.getBoolean(Settings.VIBRATE_KEY, true)) {
            builder.setVibrate(new long[]{0, 200});
        }
        builder.setSmallIcon(R.drawable.checkmark);
        builder.setContentTitle(task.getName());
        builder.setContentText(DateFormatter.printDate(task.getTimeList().get(0)) + " at " + DateFormatter.printTime(task.getTimeList().get(0)));

        Intent viewIntent = new Intent(context, TaskCreator.class);
        viewIntent.putExtra("Task", task);
        PendingIntent pViewIntent = PendingIntent.getActivity(context, requestCode, viewIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(android.R.drawable.ic_menu_search, "View", pViewIntent);
        Notification notification = builder.build();

        manager.notify(requestCode, notification);
    }
}

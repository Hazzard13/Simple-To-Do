package com.hazzard.nathan.to_do;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Task task = (Task) intent.getSerializableExtra("Task");

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(context);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[] {0, 200});
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(task.getName());
        builder.setContentText(task.printDate(task.getDate()));

        Intent viewIntent = new Intent(context, TaskCreator.class);
        viewIntent.putExtra("Task", task);
        PendingIntent pViewIntent = PendingIntent.getActivity(context, 0, viewIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.addAction(R.mipmap.ic_launcher, "View", pViewIntent);
        Notification notification = builder.build();

        manager.notify(0, notification);
    }
}

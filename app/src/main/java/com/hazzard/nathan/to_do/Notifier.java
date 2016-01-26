package com.hazzard.nathan.to_do;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class Notifier {
    private NotificationCompat.Builder builder;
    private NotificationManager manager;

    public Notifier(Context context) {
        builder = new NotificationCompat.Builder(context);
        manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        builder.setVibrate(new long[] {0, 200});
    }

    public void taskNotification(Task task) {
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(task.getName());
        builder.setContentText(task.printDate(task.getDate()));
        manager.notify(1, builder.build());
    }
}
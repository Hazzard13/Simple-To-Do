package com.hazzard.nathan.to_do;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class NotificationRefresh extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Reads in the taskList
        ArrayList taskList = MainActivity.loadTaskList(context);

        NotificationHandler notifier = new NotificationHandler(context);
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < taskList.size(); i++) {
            if(currentTime < ((Task) taskList.get(i)).getDate().getTimeInMillis()) {
                notifier.taskNotification((Task) taskList.get(i));
            }
        }
    }
}

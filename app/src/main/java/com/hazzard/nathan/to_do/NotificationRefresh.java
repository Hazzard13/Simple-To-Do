package com.hazzard.nathan.to_do;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class NotificationRefresh extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Reads in the taskList
        ArrayList taskList = TaskListManager.loadTaskList(context);

        NotificationHandler notifier = new NotificationHandler(context);
        for (int i = 0; i < taskList.size(); i++) {
            notifier.taskNotification((Task) taskList.get(i));
        }
    }
}

package com.hazzard.nathan.to_do;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class NotificationRefresh extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Reads in the taskList
        ArrayList<Task> taskList = TaskListManager.loadTaskList(context);

        NotificationHandler notifier = new NotificationHandler(context);
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if(task.repeats()) {
                while (task.getTimeList().get(0).getTimeInMillis() < System.currentTimeMillis()) {
                    task.repeat();
                }
            }
            notifier.taskNotification(task);
        }
    }
}

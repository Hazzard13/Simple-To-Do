package com.hazzard.nathan.to_do;

/**
 * NotificationRefresh is a specialised BroadcastReceiver that only runs at system launch
 * It reloads all notifications into the android alarmManager, and performs any changes that should have happened when the device was off
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class NotificationRefresh extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<Task> taskList = TaskListManager.loadTaskList(context);
        NotificationHandler notifier = new NotificationHandler(context);
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);

            //Updates any repeating tasks until they're set for a date in the future
            if(task.repeats() && task.getTimeList().get(0).getTimeInMillis() < System.currentTimeMillis()) {
                while (task.getTimeList().get(0).getTimeInMillis() < System.currentTimeMillis()) {
                    task.repeat();
                }
                taskList.remove(i);
                taskList.add(task);
            }
            notifier.taskNotification(task);
        }
        TaskListManager.saveTaskList(context, taskList);
    }
}

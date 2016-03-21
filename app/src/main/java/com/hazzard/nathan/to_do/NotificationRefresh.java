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
            if(task.repeats() && task.getTimeList().get(0).getTimeInMillis() < System.currentTimeMillis()) {
                while (task.getTimeList().get(0).getTimeInMillis() < System.currentTimeMillis()) {
                    task.repeat();
                }

                //Removes any previous versions of this task before saving it
                taskList.remove(i);

                //Saves the updated task and creates its notification
                taskList.add(task);
            }
            notifier.taskNotification(task);
        }
        TaskListManager.saveTaskList(context, taskList);
    }
}

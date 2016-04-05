package com.hazzard.nathan.to_do;

/**
 * DeleteReceiver is a specialised BroadcastReceiver that responds to the broadcasts from a task notification's delete button
 * It takes the requestCode from the broadcast intent and deletes the task from the saved taskList
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class DeleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int requestCode = (int) intent.getSerializableExtra("requestCode");
        NotificationHandler.dismissNotification(context, requestCode);

        ArrayList<Task> taskList = TaskListManager.loadTaskList(context);
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getRequestCodes().get(0).equals(requestCode)) {
                NotificationHandler.clearNotification(context, taskList.get(i));
                taskList.remove(i);
            }
        }
        TaskListManager.saveTaskList(context, taskList);
    }
}
package com.hazzard.nathan.to_do;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class NotificationRefresh extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Reads in the taskList
        ArrayList taskList = new ArrayList();
        try {
            FileInputStream inputStream = context.openFileInput(MainActivity.filename);
            byte[] byteBuffer = new byte[inputStream.available()];
            inputStream.read(byteBuffer);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteBuffer);
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            taskList = (ArrayList) objectIn.readObject();
        } catch (Exception e) {
        }

        NotificationHandler notifier = new NotificationHandler(context);
        for (int i = 0; i < taskList.size(); i++) {
            //TODO Add a test to make sure the notification hasn't already gone offNotification
            notifier.taskNotification((Task) taskList.get(i));
        }
    }
}

package com.hazzard.nathan.to_do;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;


public class TaskListManager {
    final static String FILENAME = "taskList";

    public static ArrayList<Task> loadTaskList(Context context) {
        ArrayList list = new ArrayList();
        try {
            FileInputStream inputStream = context.openFileInput(FILENAME);
            byte[] byteBuffer = new byte[inputStream.available()];
            inputStream.read(byteBuffer);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteBuffer);
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            list = (ArrayList) objectIn.readObject();
        } catch (Exception e) {
        }
        return list;
    }

    public static void saveTaskList(Context context, ArrayList taskList) {
        try {
            FileOutputStream outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(taskList);
            outputStream.write(byteOut.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final static String NAME = "name";
    final static String DATE = "date";
    final static String PRIORITY = "priority";

    //Sorts the taskList according to a selection of comparators, chosen by the value of sort
    public static ArrayList<Task> sortTaskList(String sort, ArrayList<Task> taskList)
    {
        switch (sort){
            case NAME:
                Collections.sort(taskList, new Task.NameComparator());
                break;
            case DATE:
                Collections.sort(taskList, new Task.DueDateComparator());
                break;
            case PRIORITY:
                Collections.sort(taskList, new Task.PriorityComparator());
                break;
            default:
                break;
        }
        return taskList;
    }
}

package com.hazzard.nathan.to_do;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;


public class TaskListManager {
    final static String FILENAME = "taskList";

    public static ArrayList<Task> loadTaskList(Context context) {
        ArrayList taskList = new ArrayList();
        try {
            FileInputStream inputStream = context.openFileInput(FILENAME);
            XmlPullParser xmlReader = Xml.newPullParser();
            xmlReader.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlReader.setInput(inputStream, "UTF-8");
            xmlReader.nextTag();

            xmlReader.require(XmlPullParser.START_TAG, null, "TaskList");
            while (xmlReader.next() != XmlPullParser.END_TAG) {
                if (xmlReader.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = xmlReader.getName();
                // Starts by looking for the entry tag
                if (name.equals("Task")) {
                    //Add a Task to the taskList
                    taskList.add(readTask(xmlReader));
                } else {
                    skip(xmlReader);
                }
            }
        } catch (Exception e) {
        }
        return taskList;
    }

    public static Task readTask(XmlPullParser xmlReader) throws IOException, XmlPullParserException {
        xmlReader.require(XmlPullParser.START_TAG, null, "Task");

        String taskName = xmlReader.getAttributeValue(null, "Name");
        GregorianCalendar date = new GregorianCalendar();
        date.setTimeInMillis(Long.parseLong(xmlReader.getAttributeValue(null, "Date")));
        int priority = Integer.parseInt(xmlReader.getAttributeValue(null, "Priority"));
        String details = xmlReader.getAttributeValue(null, "Details");
        int requestCode = Integer.parseInt(xmlReader.getAttributeValue(null, "RequestCode"));

        //Ensures xmlReader is at the end of the Task before moving on
        while (xmlReader.next() != XmlPullParser.END_TAG) {}
        return new Task(taskName, date, priority, details, requestCode);
    }

    private static void skip(XmlPullParser xmlReader) throws XmlPullParserException, IOException {
        if (xmlReader.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (xmlReader.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public static void saveTaskList(Context context, ArrayList taskList) {
        try {
            FileOutputStream fileOutput = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            XmlSerializer xmlWriter = Xml.newSerializer();
            xmlWriter.setOutput(fileOutput, "UTF-8");
            xmlWriter.startDocument("UTF-8", true);
            xmlWriter.startTag(null, "TaskList");

            for (int i = 0; i < taskList.size(); i++) {
                Task task = (Task) taskList.get(i);
                xmlWriter.startTag(null, "Task");
                xmlWriter.attribute(null, "Name", task.getName());
                xmlWriter.attribute(null, "Date", "" + task.getDate().getTimeInMillis());
                xmlWriter.attribute(null, "Priority", "" + task.getPriority());
                xmlWriter.attribute(null, "Details", task.getDetails());
                xmlWriter.attribute(null, "RequestCode", "" + task.getRequestCode());
                xmlWriter.endTag(null, "Task");
            }

            xmlWriter.endTag(null, "TaskList");
            xmlWriter.endDocument();
            xmlWriter.flush();
            fileOutput.close();
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

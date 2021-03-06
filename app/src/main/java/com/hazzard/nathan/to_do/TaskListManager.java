package com.hazzard.nathan.to_do;

/**
 * TaskListManager is a collection of static methods that handle all the XML work behind saving and loading the TaskList
 * It also contains the sort method
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

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

    final static String NAME = "Name";
    final static String DATE = "Date";
    final static String PRIORITY = "Priority";

    public static ArrayList<Task> sortTaskList(String sort, ArrayList<Task> taskList) {
        switch (sort) {
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

    public static void saveTaskList(Context context, ArrayList<Task> taskList) {
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
                xmlWriter.attribute(null, "Date", "" + task.getTimeList().get(0).getTimeInMillis());
                xmlWriter.attribute(null, "Repeating", "" + task.getRepeating());
                xmlWriter.attribute(null, "Priority", "" + task.getPriority());
                xmlWriter.attribute(null, "Details", task.getDetails());
                xmlWriter.attribute(null, "RequestCode", "" + task.getRequestCodes().get(0));

                for (int j = 1; j < task.getTimeList().size(); j++) {
                    xmlWriter.startTag(null, "Extra_Time");
                    xmlWriter.attribute(null, "Date", "" + task.getTimeList().get(j).getTimeInMillis());
                    xmlWriter.attribute(null, "RequestCode", "" + task.getRequestCodes().get(j));
                    xmlWriter.endTag(null, "Extra_Time");
                }

                xmlWriter.endTag(null, "Task");
            }

            xmlWriter.endTag(null, "TaskList");
            xmlWriter.endDocument();
            xmlWriter.flush();
            fileOutput.close();
        } catch (Exception e) {
            Toast.makeText(context, "Loading Failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static ArrayList<Task> loadTaskList(Context context) {
        ArrayList<Task> taskList = new ArrayList();
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
            //Toast.makeText(context, "Loading Failed", Toast.LENGTH_LONG).show();
            //e.printStackTrace();
        }
        return taskList;
    }

    private static Task readTask(XmlPullParser xmlReader) throws IOException, XmlPullParserException {
        xmlReader.require(XmlPullParser.START_TAG, null, "Task");

        String taskName = xmlReader.getAttributeValue(null, "Name");
        ArrayList<GregorianCalendar> dates = new ArrayList<GregorianCalendar>();
        GregorianCalendar date = new GregorianCalendar();
        date.setTimeInMillis(Long.parseLong(xmlReader.getAttributeValue(null, "Date")));
        dates.add(date);
        int repeating;
        if (xmlReader.getAttributeValue(null, "Repeating") != null) {
            repeating = Integer.parseInt(xmlReader.getAttributeValue(null, "Repeating"));
        } else {
            repeating = 0;
        }
        int priority = Integer.parseInt(xmlReader.getAttributeValue(null, "Priority"));
        String details = xmlReader.getAttributeValue(null, "Details");
        ArrayList<Integer> requestCodes = new ArrayList<Integer>();
        requestCodes.add(Integer.parseInt(xmlReader.getAttributeValue(null, "RequestCode")));

        while (xmlReader.next() != XmlPullParser.END_TAG) {
            String name = xmlReader.getName();
            if (name.equals("Extra_Time")) {
                date = new GregorianCalendar();
                date.setTimeInMillis(Long.parseLong(xmlReader.getAttributeValue(null, "Date")));
                dates.add(date);
                requestCodes.add(Integer.parseInt(xmlReader.getAttributeValue(null, "RequestCode")));

                while (xmlReader.next() != XmlPullParser.END_TAG) {}
            } else {
                skip(xmlReader);
            }
        }
        return new Task(taskName, dates, repeating, priority, details, requestCodes);
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
}

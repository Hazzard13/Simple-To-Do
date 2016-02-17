package com.hazzard.nathan.to_do;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Task implements Serializable {
    public String name;
    public GregorianCalendar date;
    public String details;
    public int priority;
    public int requestCode;

    public Task(String cName, GregorianCalendar cDate, int cPriority, String cDetails, int cRequestCode){
        name = cName;
        date = cDate;
        priority = cPriority;
        details = cDetails;
        requestCode = cRequestCode;
    }

    public String getName() {
        return name;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public String getDetails() {
        return details;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String toString() {
        return name + ": " + printDate(date) + "\n";
    }

    public static String printDate(GregorianCalendar date) {
        String printDate = "";
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                printDate += "Sunday, ";
                break;
            case 2:
                printDate += "Monday, ";
                break;
            case 3:
                printDate += "Tuesday, ";
                break;
            case 4:
                printDate += "Wednesday, ";
                break;
            case 5:
                printDate += "Thursday, ";
                break;
            case 6:
                printDate += "Friday, ";
                break;
            case 7:
                printDate += "Saturday, ";
                break;
        }
        switch (date.get(Calendar.MONTH)) {
            case 0:
                printDate += "Jan. ";
                break;
            case 1:
                printDate += "Feb. ";
                break;
            case 2:
                printDate += "Mar. ";
                break;
            case 3:
                printDate += "Apr. ";
                break;
            case 4:
                printDate += "May. ";
                break;
            case 5:
                printDate += "Jun. ";
                break;
            case 6:
                printDate += "Jul. ";
                break;
            case 7:
                printDate += "Aug. ";
                break;
            case 8:
                printDate += "Sep. ";
                break;
            case 9:
                printDate += "Oct. ";
                break;
            case 10:
                printDate += "Nov. ";
                break;
            case 11:
                printDate += "Dec. ";
                break;
        }
        printDate += date.get(Calendar.DAY_OF_MONTH);
        return printDate;
    }

    public static String printTime(GregorianCalendar date) {
        String printTime = "";
        switch (date.get(Calendar.HOUR)) {
            case 0:
                printTime += 12;
                break;
            default:
                printTime += date.get(Calendar.HOUR);
                break;
        }
        printTime += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            printTime += 0;
        }
        printTime += date.get(Calendar.MINUTE);
        switch (date.get(Calendar.AM_PM)) {
            case 0:
                printTime += "AM";
                break;
            case 1:
                printTime += "PM";
                break;
        }
        return printTime;
    }

    static class NameComparator implements Comparator<Task>
    {
        public int compare(Task task1, Task task2)
        {
            String t1 = task1.getName();
            String t2 = task2.getName();
            return t1.compareTo(t2);
        }
    }

    static class DueDateComparator implements Comparator<Task>
    {
        public int compare(Task task1, Task task2)
        {
            GregorianCalendar date1 = task1.getDate();
            GregorianCalendar date2 = task2.getDate();
            return date1.compareTo(date2);
        }
    }

    static class PriorityComparator implements Comparator<Task>
    {
        public int compare(Task task1, Task task2)
        {
            Integer p1 = task1.getPriority();
            Integer p2 = task2.getPriority();
            return p2 - p1;
        }
    }
}
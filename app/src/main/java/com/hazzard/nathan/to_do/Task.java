package com.hazzard.nathan.to_do;

import java.io.Serializable;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Task implements Serializable {
    public String name;
    public GregorianCalendar date;
    public int priority;
    public String details;
    public int requestCode;

    public Task(String Name, GregorianCalendar Date, int Priority, String Details, int RequestCode){
        this.name = Name;
        this.date = Date;
        this.priority = Priority;
        this.details = Details;
        this.requestCode = RequestCode;
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
        return name + ": " + DateFormatter.printDate(date) + "\n";
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
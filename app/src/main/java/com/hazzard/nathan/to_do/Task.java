package com.hazzard.nathan.to_do;

import java.io.Serializable;
import java.util.Comparator;

public class Task implements Serializable {
    public String name;
    public String date;
    public String details;

    public Task(String cName, String cDate, String cDetails){
        name = cName;
        date = cDate;
        details = cDetails;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public String toString() {
        return name + ": " + date + "\n";
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

    //TODO compare dates properly
    static class DueComparator implements Comparator<Task>
    {
        public int compare(Task task1, Task task2)
        {
            return 0;
        }
    }

    //TODO compare priorities properly
    static class PriorityComparator implements Comparator<Task>
    {
        public int compare(Task task1, Task task2)
        {
            return 0;
        }
    }
}
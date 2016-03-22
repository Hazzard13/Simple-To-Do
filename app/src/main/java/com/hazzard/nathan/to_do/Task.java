package com.hazzard.nathan.to_do;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;

public class Task implements Serializable {
    public String name;
    public ArrayList<GregorianCalendar> timeList;
    public int repeating;
    public int priority;
    public String details;
    public ArrayList<Integer> requestCodes;

    public Task(String Name, ArrayList<GregorianCalendar> timeList, int Repeating,  int Priority, String Details, ArrayList<Integer> RequestCodes){
        this.name = Name;
        this.timeList = timeList;
        this.repeating = Repeating;
        this.priority = Priority;
        this.details = Details;
        this.requestCodes = RequestCodes;
    }

    public String toString() {
        return name + ": " + DateFormatter.printDate(timeList.get(0)) + "\n";
    }

    //These values are defined in arrays.xml
    public void repeat() {
        switch(repeating) {
            case 0:
                break;
            case 1:
                //Daily repeat
                for (int i = 0; i < timeList.size(); i++) {
                    timeList.get(i).add(GregorianCalendar.DAY_OF_YEAR, 1);
                } break;
            case 2:
                //Weekly repeat
                for (int i = 0; i < timeList.size(); i++) {
                    timeList.get(i).add(GregorianCalendar.WEEK_OF_YEAR, 1);
                } break;
            case 3:
                //Biweekly repeat
                for (int i = 0; i < timeList.size(); i++) {
                    timeList.get(i).add(GregorianCalendar.WEEK_OF_YEAR, 2);
                } break;
            case 4:
                //Monthly repeat
                for (int i = 0; i < timeList.size(); i++) {
                    timeList.get(i).add(GregorianCalendar.MONTH, 1);
                } break;
        }
    }

    public Boolean isValidRequestCode(int requestCode) {
        Boolean isAvailable = true;
        for (int i = 0; i < requestCodes.size(); i++) {
            if (requestCode == requestCodes.get(i)) {
                isAvailable = false;
            }
        }
        return isAvailable;
    }

    public Boolean repeats() {
        return repeating > 0;
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
            GregorianCalendar date1 = task1.getTimeList().get(0);
            GregorianCalendar date2 = task2.getTimeList().get(0);
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

    public String getName() {
        return name;
    }

    public ArrayList<GregorianCalendar> getTimeList() {
        return timeList;
    }

    public int getRepeating() {
        return repeating;
    }

    public int getPriority() {
        return priority;
    }

    public String getDetails() {
        return details;
    }

    public ArrayList<Integer> getRequestCodes() {
        return requestCodes;
    }
}
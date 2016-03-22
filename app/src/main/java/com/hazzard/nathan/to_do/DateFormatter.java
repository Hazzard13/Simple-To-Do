package com.hazzard.nathan.to_do;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateFormatter {

    public static String printDate(GregorianCalendar date) {
        GregorianCalendar today = new GregorianCalendar();
        int timeDifferenceInDays = (int) ((date.getTimeInMillis() - today.getTimeInMillis()) / 1000 / 60 / 60 / 24);

        String displayTime = "";
        if(timeDifferenceInDays < 2 && timeDifferenceInDays >= 0 && today.get(Calendar.DAY_OF_WEEK) == date.get(Calendar.DAY_OF_WEEK)) {
            displayTime += "Today";
        } else if(timeDifferenceInDays < 7 && timeDifferenceInDays >= 0 && today.get(Calendar.DAY_OF_WEEK) != date.get(Calendar.DAY_OF_WEEK)) {
            switch (date.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    displayTime += "Sunday";
                    break;
                case 2:
                    displayTime += "Monday";
                    break;
                case 3:
                    displayTime += "Tuesday";
                    break;
                case 4:
                    displayTime += "Wednesday";
                    break;
                case 5:
                    displayTime += "Thursday";
                    break;
                case 6:
                    displayTime += "Friday";
                    break;
                case 7:
                    displayTime += "Saturday";
                    break;
            }
        } else {
            switch (date.get(Calendar.MONTH)) {
                case 0:
                    displayTime += "Jan.";
                    break;
                case 1:
                    displayTime += "Feb.";
                    break;
                case 2:
                    displayTime += "Mar.";
                    break;
                case 3:
                    displayTime += "Apr.";
                    break;
                case 4:
                    displayTime += "May.";
                    break;
                case 5:
                    displayTime += "Jun.";
                    break;
                case 6:
                    displayTime += "Jul.";
                    break;
                case 7:
                    displayTime += "Aug.";
                    break;
                case 8:
                    displayTime += "Sep.";
                    break;
                case 9:
                    displayTime += "Oct.";
                    break;
                case 10:
                    displayTime += "Nov.";
                    break;
                case 11:
                    displayTime += "Dec.";
                    break;
            }
            displayTime += " ";
            displayTime += date.get(Calendar.DAY_OF_MONTH);
        }
        return displayTime;
    }

    public static String printTime(GregorianCalendar date) {
        String printTime = "";

        if (date.get(Calendar.HOUR) == 0) {
            printTime += 12;
        } else {
            printTime += date.get(Calendar.HOUR);
        }

        printTime += ":";
        if (date.get(Calendar.MINUTE) < 10) {
            printTime += 0;
        }
        printTime += date.get(Calendar.MINUTE);

        if (date.get(Calendar.AM_PM) == 0) {
            printTime += "AM";
        } else {
            printTime += "PM";
        }

        return printTime;
    }
}

package com.hazzard.nathan.to_do;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Nathan on 2015-11-04.
 */
public class Task implements Serializable {
    public String name;
    public String date;
    public String details;

    public Task(String cName, String cDate, String cDetails){
        name = cName;
        date = cDate;
        details = cDetails;
    }

    public String toString() {
        return new String(name + ": " + date + "\n");
    }
}

package com.hazzard.nathan.to_do;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class TimeAdapter extends ArrayAdapter<GregorianCalendar> {
    private final Context context;
    private final ArrayList<GregorianCalendar> timeList;

    public TimeAdapter(Context context, ArrayList<GregorianCalendar> timeList) {
        super(context, -1, timeList);
        this.context = context;
        this.timeList = timeList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View timeView = inflater.inflate(R.layout.time_layout, parent, false);

        Button dateButton = (Button) timeView.findViewById(R.id.taskDate);
        dateButton.setText(DateFormatter.printDate(timeList.get(position)));
        dateButton.setOnClickListener(createDateListener(parent, position));

        Button timeButton = (Button) timeView.findViewById(R.id.taskTime);
        timeButton.setText(DateFormatter.printTime(timeList.get(position)));
        timeButton.setOnClickListener(createTimeListener(parent, position));

        ImageButton addNewTime = (ImageButton) timeView.findViewById(R.id.addTime);
        if (position == 0) {
            addNewTime.setBackgroundResource(android.R.drawable.ic_input_add);
        } else {
            addNewTime.setBackgroundResource(android.R.drawable.ic_delete);
            addNewTime.setOnClickListener(createDeleteListener(parent, position));
        }

        return timeView;
    }

    private View.OnClickListener createDateListener(final ViewGroup parent, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TaskEditor) parent.getContext()).showDatePickerDialog(position);
            }
        };
    }

    private View.OnClickListener createTimeListener(final ViewGroup parent, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TaskEditor) parent.getContext()).showTimePickerDialog(position);
            }
        };
    }

    private View.OnClickListener createDeleteListener(final ViewGroup parent, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TaskEditor) parent.getContext()).removeTime(position);
            }
        };
    }
}
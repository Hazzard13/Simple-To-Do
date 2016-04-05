package com.hazzard.nathan.to_do;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class TimeAdapter extends ArrayAdapter<GregorianCalendar> {
    private final Context context;
    private final ArrayList<GregorianCalendar> timeList;
    private final ListView parentView;

    public TimeAdapter(Context context, ArrayList<GregorianCalendar> timeList, ListView parentView) {
        super(context, -1, timeList);
        this.context = context;
        this.timeList = timeList;
        this.parentView = parentView;
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        setListViewHeightBasedOnChildren(parentView);
    }

    //This code is also a workaround to make the ListView work within a ScrollView. We manually update the height of the View every time the list changes
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
package com.hazzard.nathan.to_do;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<Task> {
    private final Context context;
    private final ArrayList<Task> taskList;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        super(context, -1, taskList);
        this.context = context;
        this.taskList = taskList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = taskList.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View taskView = inflater.inflate(R.layout.task_layout, parent, false);

        ImageView imageView = (ImageView) taskView.findViewById(R.id.icon);
        TextView Title = (TextView) taskView.findViewById(R.id.firstLine);
        TextView Date = (TextView) taskView.findViewById(R.id.secondLine);

        imageView.setImageResource(R.drawable.checkmark);
        Title.setText(task.getName());
        Date.setText(DateFormatter.printDate(task.getDate()) + " at " + DateFormatter.printTime(task.getDate()));

        return taskView;
    }
}

package com.hazzard.nathan.to_do;

/**
 * TaskAdapter is a modified ArrayAdapter used to load tasks into MainActivity
 * It loads each task from the array, and provides them with a view to be added to the ListView the adapter is attached to
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

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
    public View getView(int position, View taskView, ViewGroup parent) {
        Task task = taskList.get(position);
        ViewHolder viewHolder;

        if(taskView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            taskView = inflater.inflate(R.layout.task_layout, parent, false);
            viewHolder = new ViewHolder(taskView);
            taskView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)taskView.getTag();
        }

        TextView Title = viewHolder.title;
        TextView Date = viewHolder.date;

        Title.setText(task.getName());
        Date.setText(DateFormatter.printDate(task.getTimeList().get(0)) + " at " + DateFormatter.printTime(task.getTimeList().get(0)));

        return taskView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView date;

        public ViewHolder(View view) {
            imageView = (ImageView)view.findViewById(R.id.icon);
            title = (TextView)view.findViewById(R.id.firstLine);
            date = (TextView)view.findViewById(R.id.secondLine);
        }
    }
}

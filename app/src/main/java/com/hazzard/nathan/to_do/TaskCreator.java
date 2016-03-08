package com.hazzard.nathan.to_do;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class TaskCreator extends AppCompatActivity {
    public EditText taskName;
    public GregorianCalendar taskDate;
    public int taskPriority;
    public EditText taskDetails;
    public int taskRequestCode;
    public ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Task");

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        taskDate = new GregorianCalendar(year, month, day);

        taskName = (EditText) findViewById(R.id.taskName);
        taskDetails = (EditText) findViewById(R.id.taskDetails);

        taskRequestCode = (new Random()).nextInt();

        Spinner prioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(new PriorityListener());
        prioritySpinner.setSelection(2);

        //Loads the details from a passed Task if one is present
        Intent intent = getIntent();
        if (intent.hasExtra("Task")) {
            Task task = (Task) getIntent().getSerializableExtra("Task");
            taskName.setText(task.getName());
            taskDate = task.getDate();
            taskDetails.setText(task.getDetails());
            taskPriority = task.getPriority();
            taskRequestCode = task.getRequestCode();
        }

        //Displays the date on the date button
        updateButtonTimes();

        //Sets the floating action button to call saveTask
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });

        //Listener for the task name
        taskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //Changes the title to match the task name
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTitle(s.toString());
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void updateButtonTimes() {
        ((Button) findViewById(R.id.taskDate)).setText(DateFormatter.printDate(taskDate));
        ((Button) findViewById(R.id.taskTime)).setText(DateFormatter.printTime(taskDate));
    }

    public void updateTaskDate(int year, int month, int day){
        taskDate.set(taskDate.YEAR, year);
        taskDate.set(taskDate.MONTH, month);
        taskDate.set(taskDate.DATE, day);
        updateButtonTimes();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = ((TaskCreator) getActivity()).taskDate;
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Returns the user's selected date
            ((TaskCreator) getActivity()).updateTaskDate(year, month, day);
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void updateTaskTime(int hour, int minute){
        taskDate.set(taskDate.HOUR_OF_DAY, hour);
        taskDate.set(taskDate.MINUTE, minute);
        updateButtonTimes();
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = ((TaskCreator) getActivity()).taskDate;
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            ((TaskCreator) getActivity()).updateTaskTime(hourOfDay, minute);
        }

    }

    public class PriorityListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            taskPriority = pos;
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

    public void saveTask() {
        //Opens the list from memory
        taskList = TaskListManager.loadTaskList(this);

        //Removes any previous versions of this task before saving it
        for (int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getRequestCode() == taskRequestCode) {
                taskList.remove(i);
            }
        }

        //Creates the task and adds it to the list
        Task createdTask = new Task(taskName.getText().toString(), taskDate, taskPriority, taskDetails.getText().toString(), taskRequestCode);
        taskList.add(createdTask);

        //Sends a notification to the Android alarm handler
        (new NotificationHandler(this)).taskNotification(createdTask);

        //Saves the list back to memory and ends the activity
        TaskListManager.saveTaskList(this, taskList);
        Toast.makeText(TaskCreator.this, "Task Saved", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}

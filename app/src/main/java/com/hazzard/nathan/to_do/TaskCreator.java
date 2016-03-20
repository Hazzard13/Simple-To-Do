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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class TaskCreator extends AppCompatActivity {
    public EditText taskName;
    public ArrayList<GregorianCalendar> timeList;
    public int taskPriority;
    public EditText taskDetails;
    public ArrayList<Integer> taskRequestCodes;
    public ArrayList<Task> taskList;

    public TimeAdapter timeAdapter;
    public int datePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Task");
        taskList = TaskListManager.loadTaskList(this);

        taskName = (EditText) findViewById(R.id.taskName);
        taskDetails = (EditText) findViewById(R.id.taskDetails);
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
            timeList = task.getTimeList();
            taskDetails.setText(task.getDetails());
            taskPriority = task.getPriority();
            prioritySpinner.setSelection(taskPriority);
            taskRequestCodes = task.getRequestCodes();
        } else {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            timeList = new ArrayList<GregorianCalendar>();
            timeList.add(new GregorianCalendar(year, month, day));

            taskRequestCodes = new ArrayList<Integer>();
            addRequestCode();
        }

        displayTimes();

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

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTitle(s.toString());
            }
        });
    }

    public void displayTimes() {
        ListView displayTimes = (ListView) findViewById(R.id.timeList);
        timeAdapter = new TimeAdapter(this, timeList);
        displayTimes.setAdapter(timeAdapter);
    }

    public void addRequestCode() {
        Boolean validRequestCode = true;
        int newRequestCode;
        do {
            validRequestCode = true;
            newRequestCode = (new Random()).nextInt();
            for (int i = 0; i < taskList.size(); i++) {
                if(!taskList.get(i).isValidRequestCode(newRequestCode)) {
                    validRequestCode = false;
                }
            }
            for (int i = 0; i < taskRequestCodes.size(); i++) {
                if(newRequestCode == taskRequestCodes.get(i)) {
                    validRequestCode = false;
                }
            }
        } while (!validRequestCode);
        taskRequestCodes.add(newRequestCode);
    }

    public void addTime(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        timeList.add(new GregorianCalendar(year, month, day));
        addRequestCode();
        timeAdapter.notifyDataSetChanged();
    }

    public void removeTime(int position) {
        timeList.remove(position);
        taskRequestCodes.remove(position);
        timeAdapter.notifyDataSetChanged();
    }

    public void showDatePickerDialog(int position) {
        datePosition = position;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void updateTaskDate(int year, int month, int day){
        timeList.get(datePosition).set(GregorianCalendar.YEAR, year);
        timeList.get(datePosition).set(GregorianCalendar.MONTH, month);
        timeList.get(datePosition).set(GregorianCalendar.DATE, day);
        timeAdapter.notifyDataSetChanged();
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current timeList as the default timeList in the picker
            TaskCreator activity = ((TaskCreator) getActivity());
            final Calendar c = (activity.timeList.get((activity.datePosition)));
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Returns the user's selected timeList
            ((TaskCreator) getActivity()).updateTaskDate(year, month, day);
        }
    }

    public void showTimePickerDialog(int position) {
        datePosition = position;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void updateTaskTime(int hour, int minute){
        timeList.get(datePosition).set(GregorianCalendar.HOUR_OF_DAY, hour);
        timeList.get(datePosition).set(GregorianCalendar.MINUTE, minute);
        timeAdapter.notifyDataSetChanged();
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            TaskCreator activity = ((TaskCreator) getActivity());
            final Calendar c = (activity.timeList.get((activity.datePosition)));
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
        //Removes any previous versions of this task before saving it
        for (int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getRequestCodes().get(0).equals(taskRequestCodes.get(0))) {
                NotificationHandler.clearNotification(this, taskList.get(i));
                taskList.remove(i);
            }
        }

        //Creates the task and adds it to the list
        Task createdTask = new Task(taskName.getText().toString(), timeList, taskPriority, taskDetails.getText().toString(), taskRequestCodes);
        taskList.add(createdTask);

        //Sends a notification to the Android alarm handler
        (new NotificationHandler(this)).taskNotification(createdTask);

        //Saves the list back to memory and ends the activity
        TaskListManager.saveTaskList(this, taskList);
        Toast.makeText(TaskCreator.this, "Task Saved", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}

package com.hazzard.nathan.to_do;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TaskCreator extends AppCompatActivity {
    public EditText taskName;
    public GregorianCalendar taskDate;
    public int taskPriority;
    public EditText taskDetails;
    public ArrayList taskList;
    final String filename = "taskList";

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
        setTaskDate(new GregorianCalendar(year, month, day));

        taskName = (EditText) findViewById(R.id.taskName);
        taskDetails = (EditText) findViewById(R.id.taskDetails);

        Spinner prioritySpinner = (Spinner) findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(adapter);
        prioritySpinner.setOnItemSelectedListener(new PriorityListener());
        prioritySpinner.setSelection(2);

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

    public void setTaskDate(GregorianCalendar date){
        taskDate = date;
        ((Button) findViewById(R.id.taskDate)).setText(Task.printDate(taskDate));
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
            ((TaskCreator) getActivity()).setTaskDate(new GregorianCalendar(year, month, day));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
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
        taskList = new ArrayList();
        try {
            FileInputStream inputStream = openFileInput(filename);
            byte[] byteBuffer = new byte[inputStream.available()];
            inputStream.read(byteBuffer);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteBuffer);
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            taskList = (ArrayList) objectIn.readObject();
        } catch (Exception e) {
        }

        //Creates the task and adds it to the list
        Task createdTask = new Task(taskName.getText().toString(), taskDate, taskPriority, taskDetails.getText().toString());
        taskList.add(createdTask);

        //Saves the list back to memory and ends the activity
        try {
            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(taskList);
            outputStream.write(byteOut.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(TaskCreator.this, "Task Saved", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}

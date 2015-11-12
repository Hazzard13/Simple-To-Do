package com.hazzard.nathan.to_do;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class NewTask extends AppCompatActivity {
    public EditText taskName;
    public EditText taskDate;
    public EditText taskDetails;
    public ArrayList taskList;
    final String filename = "taskList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Task");

        taskName = (EditText) findViewById(R.id.taskName);
        taskDate = (EditText) findViewById(R.id.taskDate);
        taskDetails = (EditText) findViewById(R.id.taskDetails);

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
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            //Changes the title to match the task name
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTitle(s.toString());
            }
        });
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
        Task createdTask = new Task(taskName.getText().toString(), taskDate.getText().toString(), taskDetails.getText().toString());
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
        this.finish();
    }
}

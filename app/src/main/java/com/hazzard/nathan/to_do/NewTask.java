package com.hazzard.nathan.to_do;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
        Intent intent = getIntent();
        //taskList = intent.getParcelableArrayListExtra("taskList");
        taskList = new ArrayList();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Task");

        taskName = (EditText) findViewById(R.id.taskName);
        taskDate = (EditText) findViewById(R.id.taskDate);
        taskDetails = (EditText) findViewById(R.id.taskDetails);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });

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

    public void saveTask() {
        Task createdTask = new Task(taskName.getText().toString(), taskDate.getText().toString(), taskDetails.getText().toString());
        taskList.add(createdTask);

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

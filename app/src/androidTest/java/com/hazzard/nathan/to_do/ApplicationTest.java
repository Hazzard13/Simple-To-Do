package com.hazzard.nathan.to_do;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class ApplicationTest extends ApplicationTestCase<Application> {
    private Application main;
    private ArrayList<Task> storedTaskList;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        main = getApplication();
        storedTaskList = TaskListManager.loadTaskList(main);
    }

    @Override
    protected void tearDown() throws Exception {
        TaskListManager.saveTaskList(main, storedTaskList);
        super.tearDown();
    }

    public void testSaveAndLoadTaskList() {
        ArrayList <Task> taskList = new ArrayList<Task>();
        taskList.add(new Task("Sample1", new ArrayList<>(Arrays.asList(new GregorianCalendar[]{new GregorianCalendar()})), 0, 1, "Sample1", new ArrayList<>(Arrays.asList(new Integer[]{1}))));
        taskList.add(new Task("Sample2", new ArrayList<>(Arrays.asList(new GregorianCalendar[]{new GregorianCalendar()})), 0, 2, "Sample2", new ArrayList<>(Arrays.asList(new Integer[]{2}))));
        TaskListManager.saveTaskList(main, taskList);
        assert taskList.equals(TaskListManager.loadTaskList(main));
    }

    public void test100TaskTime() {
        ArrayList <Task> taskList = new ArrayList<Task>();
        for (int i = 0; i < 100; i++) {
            taskList.add(new Task("Test", new ArrayList<>(Arrays.asList(new GregorianCalendar[] {new GregorianCalendar()})), 0, 0, "Sample1", new ArrayList<>(Arrays.asList(new Integer[] {i}))));
        }
        long startTime = System.nanoTime();
        TaskListManager.saveTaskList(main, taskList);
        long endTime = System.nanoTime();
        Log.d("unitTest", "Saving 100 tasks took " + ((endTime - startTime)) + " nanoseconds");

        startTime = System.nanoTime();
        taskList = TaskListManager.loadTaskList(main);
        endTime = System.nanoTime();
        Log.d("unitTest", "Loading 100 tasks took " + ((endTime - startTime)) + " nanoseconds");
    }

    public void test1000TaskTime() {
        ArrayList <Task> taskList = new ArrayList<Task>();
        for (int i = 0; i < 1000; i++) {
            taskList.add(new Task("Test", new ArrayList<>(Arrays.asList(new GregorianCalendar[] {new GregorianCalendar()})), 0, 0, "Sample1", new ArrayList<>(Arrays.asList(new Integer[] {i}))));
        }
        long startTime = System.nanoTime();
        TaskListManager.saveTaskList(main, taskList);
        long endTime = System.nanoTime();
        Log.d("unitTest", "Saving 1000 tasks took " + ((endTime - startTime)) + " nanoseconds");

        startTime = System.nanoTime();
        taskList = TaskListManager.loadTaskList(main);
        endTime = System.nanoTime();
        Log.d("unitTest", "Loading 1000 tasks took " + ((endTime - startTime)) + " nanoseconds");
    }

    //Note: Can't test notifications, espresso explicitly states that it can't do this (Possibly can with something called UIAutomator in concert with espresso)
}
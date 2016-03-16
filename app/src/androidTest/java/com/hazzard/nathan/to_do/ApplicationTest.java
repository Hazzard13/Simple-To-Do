package com.hazzard.nathan.to_do;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
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

    public void testSaveAndLoadTaskList() {
        ArrayList <Task> taskList = new ArrayList<Task>();
        taskList.add(new Task("Sample1", new GregorianCalendar(), 2, "", 0));
        taskList.add(new Task("Sample2", new GregorianCalendar(), 2, "", 0));
        TaskListManager.saveTaskList(main, taskList);
        assert taskList.equals(TaskListManager.loadTaskList(main));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        TaskListManager.saveTaskList(main, storedTaskList);
    }
}
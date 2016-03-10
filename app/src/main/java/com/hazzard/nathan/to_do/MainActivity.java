package com.hazzard.nathan.to_do;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ArrayList<Task> taskList;
    SharedPreferences settings;
    String sortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        sortBy = settings.getString(Settings.SORT_KEY, getResources().getString(R.string.defaultSortMethod));

        //This adds the navpane to the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Sets the floating action button to create new tasks
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTask = new Intent(MainActivity.this, TaskCreator.class);
                MainActivity.this.startActivity(newTask);
            }
        });

        //Does some android magic around the navpane
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Loads the task list fresh from memory every time control goes back to this activity
    @Override
    protected void onResume() {
        super.onResume();
        taskList = TaskListManager.loadTaskList(this);
        //TODO implement a method to sortTaskList by whatever it was last sorted by (Probably Preferences)

        taskList = TaskListManager.sortTaskList(sortBy, taskList);
        displayList();
    }

    //iterates through taskList and adds every Task to displayText
    public void displayList() {
        ListView displayList = (ListView) findViewById(R.id.body);
        displayList.setAdapter(new TaskAdapter(this, taskList));
        displayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent viewTask = new Intent(view.getContext(), TaskCreator.class);
                viewTask.putExtra("Task", taskList.get(position));
                startActivity(viewTask);
            }
        });
        displayList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog deleteAlert = new AlertDialog.Builder(parent.getContext()).create();
                deleteAlert.setTitle("Delete?");
                deleteAlert.setMessage("Are you sure you want to delete " + taskList.get(position).getName() + "?");
                deleteAlert.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taskList.remove(position);
                        TaskListManager.saveTaskList(parent.getContext(), taskList);
                        ((TaskAdapter) parent.getAdapter()).notifyDataSetChanged();
                    }
                });
                deleteAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                deleteAlert.show();
                return true;
            }
        });
    }

    //Overrides the back button to close the navpane if it's open
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //This is where the navpane items are defined
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Called when an nav option is clicked, this determines what option was chosen, and reacts
        //Currently only calls my sort operations
        switch(item.getItemId()) {
            case R.id.sort_name:
                taskList = TaskListManager.sortTaskList(TaskListManager.NAME, taskList);
                Toast.makeText(MainActivity.this, "Sorted Alphabetically", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sort_date:
                taskList = TaskListManager.sortTaskList(TaskListManager.DATE, taskList);
                Toast.makeText(MainActivity.this, "Sorted by Date", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sort_priority:
                taskList = TaskListManager.sortTaskList(TaskListManager.PRIORITY, taskList);
                Toast.makeText(MainActivity.this, "Sorted by Priority", Toast.LENGTH_SHORT).show();
                break;
        }
        //Refreshes the displayList after it's sorted
        displayList();

        //Closes the navpane afterwards
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

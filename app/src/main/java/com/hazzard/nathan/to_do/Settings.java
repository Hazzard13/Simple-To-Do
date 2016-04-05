package com.hazzard.nathan.to_do;

/**
 * Settings is a simple activity that loads the SettingsFragment
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    final static String SORT_KEY = "defaultSort";
    final static String RINGTONE_KEY = "ringtone";
    final static String VIBRATE_KEY = "vibrate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}

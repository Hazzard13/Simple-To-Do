package com.hazzard.nathan.to_do;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

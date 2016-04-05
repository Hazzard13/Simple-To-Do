package com.hazzard.nathan.to_do;

/**
 * SettingsFragment is a simple fragment that loads the settings from XML
 *
 * Nathan Hazzard
 * Version 1.1.6
 */

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}

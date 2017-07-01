package com.mou.inc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by Mohannad on 14/03/2017.
 */
public class SettingsActivity extends PreferenceActivity {

    public static String commandfromPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);








    }



}


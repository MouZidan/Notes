package com.mou.inc.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;

/**
 * Created by Mohannad on 14/03/2017.
 */
public class SettingsActivity extends Activity {

    public static String commandfromPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        overridePendingTransition(0, 0);


    }



}


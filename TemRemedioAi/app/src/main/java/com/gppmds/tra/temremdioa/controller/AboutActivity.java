/**
 * File: AboutActivity.java
 * Purpose: this file set information about the app in one activity.
 */
package com.gppmds.tra.temremdioa.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tra.gppmds.temremdioa.R;

public class AboutActivity extends AppCompatActivity {

    /**
     * Method: onCreate()
     * Purpose: create the activity on the screen.
     * @return
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Make visible the activity with information about the application, not editable
        setContentView(R.layout.activity_about);
    }
}

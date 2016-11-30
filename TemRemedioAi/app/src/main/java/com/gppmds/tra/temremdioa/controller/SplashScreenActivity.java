/**
 * File: SplachScreenActivity.java
 * Purpose: this file set all paramters about the splash screen on app.
 */
package com.gppmds.tra.temremdioa.controller;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import com.tra.gppmds.temremdioa.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    private static final int waitTime = 2000;

    /**
     * Method: openMainActivity
     * Purpose: this method open the MainActivity after the splash screen was loaded.
     * @return boolean
     */
    public boolean openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Boolean valid = (Boolean)true;

        try {
            startActivity(intent);
            valid =  (Boolean) true;
        } catch (ActivityNotFoundException e) {
            valid =  (Boolean) false;
        }

        intent.finalize();

        return valid;
    }

    /**
     * Method: onCreate
     * Purpose: this method instance the content view for splasj screen.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                openMainActivity();
                finish();
            }
        }, waitTime);
    }
}

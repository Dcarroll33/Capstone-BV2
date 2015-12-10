package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

/**
 * @Author: Devlyn Carroll
 * SplashActivity is the first activity that is loaded when the application is launched. This class
 * displays a splash screen while the application loads.
 */
public class SplashActivity extends Activity {

    Handler handler;

    /**
     * Assigns five second splash screen display time to the splash screen.
     */
    static final int DELAY = 5000;

    /**
     * Assigns the nextActivity as the login screen.
     */
    Class nextActivity = Login_Screen.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    /**
     * onStart method used for when the activity starts it creates a new handler and sets a
     * postDelayed based off the runnable and delay parameters passed in.
     */
    public void onStart(){
        super.onStart();
        handler = new Handler();
        handler.postDelayed(r,DELAY);
    }


    /**
     * Runnable is a thread that runs the goToNextScreen method.
     */
    Runnable r = new Runnable(){
      public void run() {
          gotoNextScreen();
      }
    };

    /**
     * Method to fire the next activity after the splash screen displays.
     */
    public void gotoNextScreen() {
        Intent nextScreen = new Intent(this, nextActivity);

        this.startActivity(nextScreen);
    }
}

package com.hw1.devlyn.thewateringhole1;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;


public class SplashActivity extends Activity {

    Handler handler;

    /*Assigns five second splash screen display time to the splash screen.*/
    static final int DELAY = 5000;

    /*Assigns the nextActivity as the login screen*/
    Class nextActivity = Login_Screen.class;

    private AccountManager mAccountManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    public void onStart(){
        super.onStart();
        handler = new Handler();
        handler.postDelayed(r,DELAY);
    }



    Runnable r = new Runnable(){
      public void run() {
          gotoNextScreen();
      }
    };

    /*Method to fire the next activity after the splash screen displays.*/
    public void gotoNextScreen() {
        Intent nextScreen = new Intent(this, nextActivity);
        Bundle bundle = new Bundle();

        this.startActivity(nextScreen);
    }
}

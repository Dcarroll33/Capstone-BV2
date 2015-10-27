package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Login_Screen extends Activity implements View.OnClickListener {

    /*Field declarations for the buttons login and register*/
    Button login;
    Button register;

    /*Fields for the EditText's on the login screen*/
    EditText userNameText;
    EditText userNamePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);

        /*Assigns the login and register button to the buttons in the XML layout by their ID's*/
        login = (Button) this.findViewById(R.id.login_button);
        register = (Button) this.findViewById(R.id.register_button);

        /*Assigns the userNameText and userNamePass to the EditTexts in the XML layout by their ID's*/
        userNameText = (EditText) this.findViewById(R.id.userNameText);
        userNamePass = (EditText) this.findViewById(R.id.userNamePass);

        /*Runs the listener for button clicks in this instance for login and register*/
        login.setOnClickListener(this);
        register.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /*This method is used to listen for user clicks on the login & register buttons that are
        displayed on the screen*/
    public void onClick(View view) {
        ConnectDb conDb = new ConnectDb();
        /*Checks to see if the click is the login else if the click is on the register button*/
        if (view == login) {
            /*These are the userName and userPass strings that store the strings from the EditTexts
              that are in the layout. Once the strings are stored they are passed as parameters and
              are sent to the DB. */
            String userName = userNameText.getText().toString();
            String userPass = userNamePass.getText().toString();
            String[] params = {"login", userName, userPass};
            try {
                conDb.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            String userId = String.valueOf(conDb.getUserId());

            ArrayList<String> myList = conDb.getUserProfileInfo();
            String idUserProfile = myList.get(1);
            String userNameP = myList.get(2);
            String description = myList.get(3);
            String events = myList.get(4);
            String likes_dislikes = myList.get(5);

            /*This is a check to make sure the userId is valid and if it is then we have logged in
                successfully. This also starts an intent for the MainActivity and stores the userId,
                description and likes_dislikes as extras to pass to MainActivity. */
            if(!userId.equals("-1")){
                Toast.makeText(getBaseContext(), "Logged in", Toast.LENGTH_SHORT).show();

                Intent login = new Intent(this, MainActivity.class );
                login.putExtra("userId", userId);
                login.putExtra("idUserProfile", idUserProfile);
                login.putExtra("userName", userNameP);
                login.putExtra("description", "" + description );
                login.putExtra("events", events);
                login.putExtra("likes_dislikes", likes_dislikes);

                this.startActivity(login);
            }else {
                Toast.makeText(getBaseContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        /*This is the intent that fires when the register button has been clicked and launches the RegisterActivity.*/
        }else if (view == register) {
            Intent register = new Intent(this, RegisterActivity.class);

            this.startActivity(register);
        }
    }
}

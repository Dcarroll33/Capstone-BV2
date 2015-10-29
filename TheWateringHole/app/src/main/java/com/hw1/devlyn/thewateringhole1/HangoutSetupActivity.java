package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HangoutSetupActivity extends Activity implements View.OnClickListener {

    String currentUser;
    String idUserProfile;
    String description;
    String likes_dislikes;
    String userName;
    String events;

    Button Finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangout_setup);

        Intent thisIntent = getIntent();
        currentUser = thisIntent.getStringExtra("userId");
        idUserProfile = thisIntent.getStringExtra("idUserProfile");
        userName = thisIntent.getStringExtra("userName");
        description = thisIntent.getStringExtra("description");
        events = thisIntent.getStringExtra("events");
        likes_dislikes = thisIntent.getStringExtra("likes_dislikes");

        Finish = (Button) this.findViewById(R.id.finish_btn);

        Finish.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == Finish) {
            Intent Finish = new Intent(this, HangoutPageActivity.class);

            Button b = (Button) v;
            this.startActivity(Finish);
        }
    }


}

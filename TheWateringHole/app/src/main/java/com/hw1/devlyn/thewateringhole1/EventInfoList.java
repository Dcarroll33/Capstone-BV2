package com.hw1.devlyn.thewateringhole1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by drcar on 11/17/2015.
 */
public class EventInfoList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<String> eventInfo;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> friendsList;

    private double userLongitude;
    private double userLatitude;

    private String currentUser;

    private ListView eventInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        /*Intent created to store the values that are retrieved from the MainActivity.*/
        Intent thisIntent = getIntent();
        currentUser = thisIntent.getStringExtra("userId");
        userLongitude = thisIntent.getDoubleExtra("userLongitude", userLongitude);
        userLatitude = thisIntent.getDoubleExtra("userLatitude", userLatitude);
        eventInfo = thisIntent.getStringArrayListExtra("eventInfo");
        userProfileInfo = thisIntent.getStringArrayListExtra("userProfileInfo");
        friendsList = thisIntent.getStringArrayListExtra("friendsList");

        eventInfoList = (ListView) findViewById(R.id.eventInfoList);

        for(int i = 0 ; i < eventInfo.size(); i = i + 3) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    eventInfo);
            eventInfoList.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

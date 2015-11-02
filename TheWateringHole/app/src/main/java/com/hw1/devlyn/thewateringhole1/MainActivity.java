package com.hw1.devlyn.thewateringhole1;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import info.info.wateringhole.slidingmenu.adapter.NavDrawerListAdapter;
import info.info.wateringhole.slidingmenu.model.NavDrawerItem;

import static com.hw1.devlyn.thewateringhole1.R.*;

public class MainActivity extends AppCompatActivity {

    /*Fields for the data that is being passed in from the bundle.*/
    private String currentUser;
    private String idUserProfile;
    private String description;
    private String likes_dislikes;
    private String userName;
    private String events;
    private double userLongitude;
    private double userLatitude;
    private String eventNameInfo;
    private String numParticipatingInfo;
    private String eventDescriptionInfo;

    /*These fields are used for the navigation slide out menu.*/
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    /*Navigation drawer title*/
    private CharSequence mDrawerTitle;

    /*Stores app title for menu*/
    private CharSequence mTitle;

    /*Slide menu items*/
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent thisIntent = getIntent();
        currentUser = thisIntent.getStringExtra("userId");
        idUserProfile = thisIntent.getStringExtra("idUserProfile");
        userName = thisIntent.getStringExtra("userName");
        description = thisIntent.getStringExtra("description");
        events = thisIntent.getStringExtra("events");
        likes_dislikes = thisIntent.getStringExtra("likes_dislikes");
        userLongitude = thisIntent.getDoubleExtra("userLongitude", userLongitude);
        userLatitude = thisIntent.getDoubleExtra("userLatitude", userLatitude);
        eventNameInfo = thisIntent.getStringExtra("eventName");
        numParticipatingInfo = thisIntent.getStringExtra("numParticipating");
        eventDescriptionInfo = thisIntent.getStringExtra("eventDescription");


        Fragment fragment = new Fragment();
                Bundle bundle = new Bundle();
                bundle.putString("currentUser", currentUser);
                bundle.putString("idUserProfile", idUserProfile);
                bundle.putString("userName", userName);
                bundle.putString("description", description);
                bundle.putString("events", events);
                bundle.putString("likes_dislikes", likes_dislikes);
                bundle.putDouble("userLongitude", userLongitude);
                bundle.putDouble("userLatitude", userLatitude);
                bundle.putString("eventName", eventNameInfo);
                bundle.putString("numParticipating", numParticipatingInfo);
                bundle.putString("eventDescription", eventDescriptionInfo);
                fragment.setArguments(bundle);


        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slider_menu);

        /*Log.d("Main Page","mDrawerList created"+ mDrawerList);*/
        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Photos
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Communities, Will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Pages
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // What's hot, We  will add a counter here
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);

        mDrawerList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /* This enables the action bar app icon and allows it to behave as a toggle button on the
            screen.*/

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, //nav menu toggle icon
                string.app_name, // nav drawer open - description for accessibility
                string.app_name) // nav drawer close - description for accessibility

        {
            /*These methods are used for when the menu slides out or in and what to display on the
                screen.*/
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_slide_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    /**
     * Slide menu item click listener that implements an OnItemClickListener for the listview.
     * Based off the position of the item in the navigation drawer the switch statement below fires
     * intents to start a new activity pending each case.
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);

            switch (position) {
                /*Case 0 used for the home item in the list and redirects the user to the main
                 *activity page.
                 */
                case 0:
                    Intent home = new Intent(MainActivity.this, MainActivity.class);

                    /*startActivity(home);*/
                    break;
                /*Case 1 used for the FindPeople item in the list and redirects the user to the
                 *locate friends activity page.
                 */
                case 1:
                    Intent FindPeople = new Intent(MainActivity.this, LocateFriendsActivity.class);

                    FindPeople.putExtra("userId", currentUser);
                    FindPeople.putExtra("idUserProfile", idUserProfile);
                    FindPeople.putExtra("userName", userName);
                    FindPeople.putExtra("description", "" + description);
                    FindPeople.putExtra("events", events);
                    FindPeople.putExtra("likes_dislikes", likes_dislikes);
                    FindPeople.putExtra("userLongitude", userLongitude);
                    FindPeople.putExtra("userLatitude", userLatitude);

                    android.app.Fragment FindPeopleFrag = new android.app.Fragment();
                    Bundle FindPeopleBundle = new Bundle();
                    FindPeopleBundle.putString("currentUser", currentUser);
                    FindPeopleBundle.putString("idUserProfile", idUserProfile);
                    FindPeopleBundle.putString("userName", userName);
                    FindPeopleBundle.putString("description", description);
                    FindPeopleBundle.putString("events", events);
                    FindPeopleBundle.putString("likes_dislikes", likes_dislikes);
                    FindPeopleBundle.putDouble("userLongitude", userLongitude);
                    FindPeopleBundle.putDouble("userLatitude", userLatitude);
                    FindPeopleFrag.setArguments(FindPeopleBundle);

                    startActivity(FindPeople);
                    break;
                /*Case 2 used for the FindEvents item in the list and redirects the user to the
                 *locate event activity page.
                 */
                case 2:
                    Intent FindEvents = new Intent(MainActivity.this, LocateEventsActivity.class);

                    FindEvents.putExtra("userId", currentUser);
                    FindEvents.putExtra("idUserProfile", idUserProfile);
                    FindEvents.putExtra("userName", userName);
                    FindEvents.putExtra("description", "" + description);
                    FindEvents.putExtra("events", events);
                    FindEvents.putExtra("likes_dislikes", likes_dislikes);

                    android.app.Fragment FindEventsFrag = new android.app.Fragment();
                    Bundle FindEventsBundle = new Bundle();
                    FindEventsBundle.putString("currentUser", currentUser);
                    FindEventsBundle.putString("idUserProfile", idUserProfile);
                    FindEventsBundle.putString("userName", userName);
                    FindEventsBundle.putString("description", description);
                    FindEventsBundle.putString("events", events);
                    FindEventsBundle.putString("likes_dislikes", likes_dislikes);
                    FindEventsFrag.setArguments(FindEventsBundle);

                    startActivity(FindEvents);
                    break;
                /*Case 3 used for the FindHangouts item in the list and redirects the user to the
                 *locate hangouts activity page.
                    */
                case 3:
                    Intent FindHangouts = new Intent(MainActivity.this, LocateHangoutActivity.class);

                    FindHangouts.putExtra("userId", currentUser);
                    FindHangouts.putExtra("idUserProfile", idUserProfile);
                    FindHangouts.putExtra("userName", userName);
                    FindHangouts.putExtra("description", "" + description);
                    FindHangouts.putExtra("events", events);
                    FindHangouts.putExtra("likes_dislikes", likes_dislikes);

                    android.app.Fragment FindHangoutsFrag = new android.app.Fragment();
                    Bundle FindHangoutsBundle = new Bundle();
                    FindHangoutsBundle.putString("currentUser", currentUser);
                    FindHangoutsBundle.putString("idUserProfile", idUserProfile);
                    FindHangoutsBundle.putString("userName", userName);
                    FindHangoutsBundle.putString("description", description);
                    FindHangoutsBundle.putString("events", events);
                    FindHangoutsBundle.putString("likes_dislikes", likes_dislikes);
                    FindHangoutsFrag.setArguments(FindHangoutsBundle);

                    startActivity(FindHangouts);
                    break;
                /*Case 4 used for the Edit Profile item in the list and redirects the user to the
                 *profile activity page.
                 */
                case 4:
                    Intent EditProfile = new Intent(MainActivity.this, EditProfileActivity.class);
                    EditProfile.putExtra("userId", currentUser);
                    EditProfile.putExtra("idUserProfile", idUserProfile);
                    EditProfile.putExtra("userName", userName);
                    EditProfile.putExtra("description", "" + description);
                    EditProfile.putExtra("events", events);
                    EditProfile.putExtra("likes_dislikes", likes_dislikes);
                    EditProfile.putExtra("userLongitude", userLongitude);
                    EditProfile.putExtra("userLatitude", userLatitude);

                    android.app.Fragment EditProfileFrag = new android.app.Fragment();
                    Bundle EditProfileBundle = new Bundle();
                    EditProfileBundle.putString("currentUser", currentUser);
                    EditProfileBundle.putString("idUserProfile", idUserProfile);
                    EditProfileBundle.putString("userName", userName);
                    EditProfileBundle.putString("description", description);
                    EditProfileBundle.putString("events", events);
                    EditProfileBundle.putString("likes_dislikes", likes_dislikes);
                    EditProfileBundle.putDouble("userLongitude", userLongitude);
                    EditProfileBundle.putDouble("userLatitude", userLatitude);
                    EditProfileFrag.setArguments(EditProfileBundle);

                    startActivity(EditProfile);
                    break;

                /*Case 5 used for the Settings item in the list and redirects the user to the
                 *settings activity page.
                 */
                case 5:
                    Intent Settings = new Intent(MainActivity.this, SettingsActivity.class);

                    Settings.putExtra("userId", currentUser);
                    Settings.putExtra("idUserProfile", idUserProfile);
                    Settings.putExtra("userName", userName);
                    Settings.putExtra("description", "" + description);
                    Settings.putExtra("events", events);
                    Settings.putExtra("likes_dislikes", likes_dislikes);

                    android.app.Fragment SettingsFrag = new android.app.Fragment();
                    Bundle SettingsBundle = new Bundle();
                    SettingsBundle.putString("currentUser", currentUser);
                    SettingsBundle.putString("idUserProfile", idUserProfile);
                    SettingsBundle.putString("userName", userName);
                    SettingsBundle.putString("description", description);
                    SettingsBundle.putString("events", events);
                    SettingsBundle.putString("likes_dislikes", likes_dislikes);
                    SettingsFrag.setArguments(SettingsBundle);

                    startActivity(Settings);
                    break;
                default:

            }
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


}

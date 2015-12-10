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

/**
 * @Author: Devlyn Carroll
 * MainActivity is used to set up the main page of the application using the main_activity layout.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Fields for the data that is being passed in from the bundle.
     */
    private String currentUser;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> friendsList;
    private ArrayList<String> eventInfo;
    private ArrayList<String> eventTitles;
    private ArrayList<String> eventLocation;
    private String userImageUri;

    /**
     * These fields are used for the navigation slide out menu.
     */
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


    /*onCreate method that retrieves the values from the intent that is being passed in from the
      LoginActivity.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent thisIntent = getIntent();
            currentUser = thisIntent.getStringExtra("userId");
            userProfileInfo = thisIntent.getStringArrayListExtra("userProfileInfo");
            friendsList = thisIntent.getStringArrayListExtra("friendsList");
            eventInfo = thisIntent.getStringArrayListExtra("eventInfo");
            eventTitles = thisIntent.getStringArrayListExtra("eventTitles");
            eventLocation = thisIntent.getStringArrayListExtra("eventLocation");

            //userImageUri = thisIntent.getStringExtra("userImageUri");

        /* This is where a new fragment object is initialized*/
        Fragment fragment = new Fragment();
                /*A bundle is created here, where the values from the intent are then passed in to.*/
                Bundle bundle = new Bundle();
                bundle.putString("currentUser", currentUser);
                bundle.putStringArrayList("userProfileInfo", userProfileInfo);
                bundle.putStringArrayList("eventInfo", eventInfo);
                bundle.putStringArrayList("eventTitles", eventTitles);
                bundle.putStringArrayList("eventLocation", eventLocation);
                //bundle.putString("userImageUri", userImageUri);
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
        // Find Events
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        // Edit Profile
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Settings
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Sign out
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
                    FindPeople.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    FindPeople.putStringArrayListExtra("friendsList", friendsList);
                    FindPeople.putStringArrayListExtra("eventInfo", eventInfo);
                    FindPeople.putStringArrayListExtra("eventTitles", eventTitles);
                    FindPeople.putStringArrayListExtra("eventLocation", eventLocation);
                    //FindPeople.putExtra("userImageUri", userImageUri);*/

                    android.app.Fragment FindPeopleFrag = new android.app.Fragment();
                    Bundle FindPeopleBundle = new Bundle();
                    FindPeopleBundle.putString("currentUser", currentUser);
                    FindPeopleBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    FindPeopleBundle.putStringArrayList("friendsList", friendsList);
                    FindPeopleBundle.putStringArrayList("eventInfo", eventInfo);
                    FindPeopleBundle.putStringArrayList("eventTitles", eventTitles);
                    FindPeopleBundle.putStringArrayList("eventLocation", eventLocation);
                    //FindPeopleBundle.putString("userImageUri", userImageUri);
                    FindPeopleFrag.setArguments(FindPeopleBundle);

                    startActivity(FindPeople);
                    break;
                /*Case 2 used for the FindEvents item in the list and redirects the user to the
                 *locate event activity page.
                 */
                case 2:
                    Intent Events = new Intent(MainActivity.this, EventsActivity.class);

                    Events.putExtra("userId", currentUser);
                    Events.putStringArrayListExtra("eventInfo", eventInfo);
                    Events.putStringArrayListExtra("eventTitles", eventTitles);
                    Events.putStringArrayListExtra("eventLocation", eventLocation);
                    Events.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    Events.putStringArrayListExtra("friendsList", friendsList);


                    android.app.Fragment FindEventsFrag = new android.app.Fragment();
                    Bundle FindEventsBundle = new Bundle();
                    FindEventsBundle.putString("currentUser", currentUser);
                    FindEventsBundle.putStringArrayList("eventInfo", eventInfo);
                    FindEventsBundle.putStringArrayList("eventTitles", eventTitles);
                    FindEventsBundle.putStringArrayList("eventLocation", eventLocation);
                    FindEventsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    FindEventsBundle.putStringArrayList("friendsList", friendsList);
                    FindEventsFrag.setArguments(FindEventsBundle);

                    startActivity(Events);
                    break;

                /*Case 4 used for the Edit Profile item in the list and redirects the user to the
                 *profile activity page.
                 */
                case 3:
                    Intent EditProfile = new Intent(MainActivity.this, EditProfileActivity.class);
                    EditProfile.putExtra("userId", currentUser);
                    EditProfile.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    EditProfile.putStringArrayListExtra("eventInfo", eventInfo);
                    EditProfile.putStringArrayListExtra("eventTitles", eventTitles);
                    EditProfile.putStringArrayListExtra("eventLocation", eventLocation);
                    EditProfile.putStringArrayListExtra("friendsList", friendsList);

                    android.app.Fragment EditProfileFrag = new android.app.Fragment();
                    Bundle EditProfileBundle = new Bundle();
                    EditProfileBundle.putString("currentUser", currentUser);
                    EditProfileBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    EditProfileBundle.putStringArrayList("eventInfo", eventInfo);
                    EditProfileBundle.putStringArrayList("eventTitles", eventTitles);
                    EditProfileBundle.putStringArrayList("eventLocation", eventLocation);
                    EditProfileBundle.putStringArrayList("friendsList", friendsList);
                    EditProfileFrag.setArguments(EditProfileBundle);

                    startActivity(EditProfile);
                    break;

                /*Case 5 used for the Settings item in the list and redirects the user to the
                 *settings activity page.
                 */
                case 4:
                    Intent Settings = new Intent(MainActivity.this, SettingsActivity.class);

                    Settings.putExtra("userId", currentUser);
                    Settings.putStringArrayListExtra("eventInfo", eventInfo);
                    Settings.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    Settings.putStringArrayListExtra("eventTitles", eventTitles);
                    Settings.putStringArrayListExtra("eventLocation", eventLocation);
                    Settings.putStringArrayListExtra("friendsList", friendsList);

                    android.app.Fragment SettingsFrag = new android.app.Fragment();
                    Bundle SettingsBundle = new Bundle();
                    SettingsBundle.putString("currentUser", currentUser);
                    SettingsBundle.putStringArrayList("eventInfo", eventInfo);
                    SettingsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    SettingsBundle.putStringArrayList("eventTitles", eventTitles);
                    SettingsBundle.putStringArrayList("eventLocation", eventLocation);
                    SettingsBundle.putStringArrayList("friendsList", friendsList);
                    SettingsFrag.setArguments(SettingsBundle);

                    startActivity(Settings);
                    break;
                case 5:
                    Intent main = new Intent(MainActivity.this, Login_Screen.class);
                    startActivity(main);
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

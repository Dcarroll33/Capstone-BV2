package com.hw1.devlyn.thewateringhole1;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
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


public class SettingsActivity extends AppCompatActivity {

    String currentUser;
    String idUserProfile;
    String description;
    String likes_dislikes;
    String userName;
    String events;
    double userLongitude;
    double userLatitude;

    private ArrayList<String> userProfileInfo;
    private ArrayList<String> eventInfo;
    private ArrayList<String> friendsList;
    private ArrayList<String> eventTitles;
    private ArrayList<String> eventLocation;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;


    Button locate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent thisIntent = getIntent();
        currentUser = thisIntent.getStringExtra("userId");
        idUserProfile = thisIntent.getStringExtra("idUserProfile");
        userName = thisIntent.getStringExtra("userName");
        description = thisIntent.getStringExtra("description");
        events = thisIntent.getStringExtra("events");
        likes_dislikes = thisIntent.getStringExtra("likes_dislikes");
        userProfileInfo = thisIntent.getStringArrayListExtra("userProfileInfo");
        eventInfo = thisIntent.getStringArrayListExtra("eventInfo");
        friendsList = thisIntent.getStringArrayListExtra("friendsList");
        eventTitles = thisIntent.getStringArrayListExtra("eventTitles");
        eventLocation = thisIntent.getStringArrayListExtra("eventLocation");
        userLongitude = thisIntent.getDoubleExtra("userLongitude", userLongitude);
        userLatitude = thisIntent.getDoubleExtra("userLatitude", userLatitude);

        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("currentUser", currentUser);
        bundle.putStringArrayList("eventInfo", eventInfo);
        bundle.putStringArrayList("userProfileInfo", userProfileInfo);
        bundle.putStringArrayList("friendsList", friendsList);
        bundle.putStringArrayList("eventTitles", eventTitles);
        bundle.putStringArrayList("eventLocation", eventLocation);
        /*bundle.putString("idUserProfile", idUserProfile);
        bundle.putString("userName", userName);
        bundle.putString("description", description);
        bundle.putString("events", events);
        bundle.putString("likes_dislikes", likes_dislikes);*/
        bundle.putDouble("userLongitude", userLongitude);
        bundle.putDouble("userLatitude", userLatitude);
        fragment.setArguments(bundle);

        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slider_menu);

        Log.d("Main Page", "mDrawerList created" + mDrawerList);
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

        // enabling action bar app icon and behaving it as toggle button

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name) // nav drawer close - description for accessibility

        {

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
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
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
     * Slide menu item click listener
     * */
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
                    Intent home = new Intent(SettingsActivity.this, MainActivity.class);

                    home.putExtra("userId", currentUser);
                    home.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    home.putStringArrayListExtra("friendsList", friendsList);
                    home.putStringArrayListExtra("eventInfo", eventInfo);
                    home.putStringArrayListExtra("eventTitles", eventTitles);
                    home.putStringArrayListExtra("eventLocation", eventLocation);
                    home.putExtra("userLongitude", userLongitude);
                    home.putExtra("userLatitude", userLatitude);

                    android.app.Fragment MainActivityFrag = new android.app.Fragment();
                    Bundle MainActivityBundle = new Bundle();
                    MainActivityBundle.putString("currentUser", currentUser);
                    MainActivityBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    MainActivityBundle.putStringArrayList("eventInfo", eventInfo);
                    MainActivityBundle.putStringArrayList("friendsList", friendsList);
                    MainActivityBundle.putStringArrayList("eventTitles", eventTitles);
                    MainActivityBundle.putStringArrayList("eventLocation", eventLocation);
                    MainActivityFrag.setArguments(MainActivityBundle);

                    startActivity(home);
                    break;
                /*Case 1 used for the FindPeople item in the list and redirects the user to the
                 *locate friends activity page.
                 */
                case 1:
                    Intent FindPeople = new Intent(SettingsActivity.this, LocateFriendsActivity.class);

                    FindPeople.putExtra("userId", currentUser);
                    FindPeople.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    FindPeople.putStringArrayListExtra("friendsList", friendsList);
                    FindPeople.putStringArrayListExtra("eventInfo", eventInfo);
                    FindPeople.putStringArrayListExtra("eventTitles", eventTitles);
                    FindPeople.putStringArrayListExtra("eventLocation", eventLocation);
                    FindPeople.putExtra("userLongitude", userLongitude);
                    FindPeople.putExtra("userLatitude", userLatitude);
                    //FindPeople.putExtra("userImageUri", userImageUri);*/

                    android.app.Fragment FindPeopleFrag = new android.app.Fragment();
                    Bundle FindPeopleBundle = new Bundle();
                    FindPeopleBundle.putString("currentUser", currentUser);
                    FindPeopleBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    FindPeopleBundle.putStringArrayList("friendsList", friendsList);
                    FindPeopleBundle.putStringArrayList("eventInfo", eventInfo);
                    FindPeopleBundle.putStringArrayList("eventTitles", eventTitles);
                    FindPeopleBundle.putStringArrayList("eventLocation", eventLocation);
                    FindPeopleBundle.putDouble("userLongitude", userLongitude);
                    FindPeopleBundle.putDouble("userLatitude", userLatitude);
                    //FindPeopleBundle.putString("userImageUri", userImageUri);
                    FindPeopleFrag.setArguments(FindPeopleBundle);

                    startActivity(FindPeople);
                    break;
                /*Case 2 used for the FindEvents item in the list and redirects the user to the
                *locate event activity page.
                */
                case 2:
                    Intent Events = new Intent(SettingsActivity.this, EventsActivity.class);

                    Events.putExtra("userId", currentUser);
                    Events.putStringArrayListExtra("eventInfo", eventInfo);
                    Events.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    Events.putStringArrayListExtra("friendsList", friendsList);
                    Events.putStringArrayListExtra("eventTitles", eventTitles);
                    Events.putStringArrayListExtra("eventLocation", eventLocation);


                    android.app.Fragment FindEventsFrag = new android.app.Fragment();
                    Bundle FindEventsBundle = new Bundle();
                    FindEventsBundle.putString("currentUser", currentUser);
                    FindEventsBundle.putStringArrayList("eventInfo", eventInfo);
                    FindEventsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    FindEventsBundle.putStringArrayList("friendsList", friendsList);
                    FindEventsBundle.putStringArrayList("eventTitles", eventTitles);
                    FindEventsBundle.putStringArrayList("eventLocation", eventLocation);
                    FindEventsFrag.setArguments(FindEventsBundle);

                    startActivity(Events);
                    break;
                /*Case 3 used for the FindHangouts item in the list and redirects the user to the
                 *locate hangouts activity page.
                 */
                case 3:
                    Intent EditProfile = new Intent(SettingsActivity.this, EditProfileActivity.class);

                    EditProfile.putExtra("userId", currentUser);
                    EditProfile.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    EditProfile.putStringArrayListExtra("eventInfo", eventInfo);
                    EditProfile.putStringArrayListExtra("friendsList", friendsList);
                    EditProfile.putStringArrayListExtra("eventTitles", eventTitles);
                    EditProfile.putStringArrayListExtra("eventLocation", eventLocation);
                    EditProfile.putExtra("userLongitude", userLongitude);
                    EditProfile.putExtra("userLatitude", userLatitude);

                    android.app.Fragment EditProfileFrag = new android.app.Fragment();
                    Bundle EditProfileBundle = new Bundle();
                    EditProfileBundle.putString("currentUser", currentUser);
                    EditProfileBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    EditProfileBundle.putStringArrayList("eventInfo", eventInfo);
                    EditProfileBundle.putStringArrayList("friendsList", friendsList);
                    EditProfileBundle.putStringArrayList("eventTitles", eventTitles);
                    EditProfileBundle.putStringArrayList("eventLocation", eventLocation);
                    EditProfileBundle.putDouble("userLongitude", userLongitude);
                    EditProfileBundle.putDouble("userLatitude", userLatitude);
                    EditProfileFrag.setArguments(EditProfileBundle);

                    startActivity(EditProfile);
                    break;
                /*Case 4 used for the Edit Profile item in the list and redirects the user to the
                *profile activity page.
                */
                case 4:
                    Intent Settings = new Intent(SettingsActivity.this, SettingsActivity.class);

                    Settings.putExtra("userId", currentUser);
                    Settings.putStringArrayListExtra("eventInfo", eventInfo);
                    Settings.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    Settings.putStringArrayListExtra("eventTitles", eventTitles);
                    Settings.putStringArrayListExtra("eventLocation", eventLocation);
                    Settings.putStringArrayListExtra("friendsList", friendsList);
                    Settings.putExtra("userLongitude", userLongitude);
                    Settings.putExtra("userLatitude", userLatitude);

                    android.app.Fragment SettingsFrag = new android.app.Fragment();
                    Bundle SettingsBundle = new Bundle();
                    SettingsBundle.putString("currentUser", currentUser);
                    SettingsBundle.putStringArrayList("eventInfo", eventInfo);
                    SettingsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    SettingsBundle.putStringArrayList("eventTitles", eventTitles);
                    SettingsBundle.putStringArrayList("eventLocation", eventLocation);
                    SettingsBundle.putStringArrayList("friendsList", friendsList);
                    SettingsBundle.putDouble("userLongitude", userLongitude);
                    SettingsBundle.putDouble("userLatitude", userLatitude);
                    SettingsFrag.setArguments(SettingsBundle);

                    startActivity(Settings);
                    break;
                /*Case 5 used for the Settings item in the list and redirects the user to the
                 *settings activity page.
                 */
                case 5:
                    Intent main = new Intent(SettingsActivity.this, Login_Screen.class);
                    startActivity(main);
                    break;
                default:

            }
        }
    }
    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}
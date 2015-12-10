package com.hw1.devlyn.thewateringhole1;

/*import android.app.Fragment;*/
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

/**
 * @Author: Devlyn Carroll
 * LocateFriendsActivity is used to set up the sliding menu to be used with the fragment and pass
 * the friends information to the fragment.
 */
public class LocateFriendsActivity extends AppCompatActivity {

    /**
     * Global fields used to store values retrieved from the bundle and intent to be used within
     * this class.
     */
    private String currentUser;
    private ArrayList<String> friendsList;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> eventInfo;
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
    /*public static FragmentManager fragmentManager;*/

    /**
     * onCreate is used to set up the layout and assign the values retrieved from the intent and
     * bundle to local variables used within this class.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate__friends);

        Intent thisIntent = getIntent();
        currentUser = thisIntent.getStringExtra("userId");
        friendsList = thisIntent.getStringArrayListExtra("friendsList");
        userProfileInfo = thisIntent.getStringArrayListExtra("userProfileInfo");
        eventInfo = thisIntent.getStringArrayListExtra("eventInfo");
        eventTitles = thisIntent.getStringArrayListExtra("eventTitles");
        eventLocation = thisIntent.getStringArrayListExtra("eventLocation");


        /* This is where a new fragment object is initialized*/
        Fragment fragment = new Fragment();
                /*A bundle is created here, where the values from the intent are then passed in to.*/
        Bundle bundle = new Bundle();
        bundle.putString("currentUser", currentUser);
        bundle.putStringArrayList("friendsList", friendsList);
        bundle.putStringArrayList("userProfileInfo", userProfileInfo);
        bundle.putStringArrayList("eventInfo", eventInfo);
        bundle.putStringArrayList("eventTitles", eventTitles);
        bundle.putStringArrayList("eventLocation", eventLocation);
        //bundle.putString("userImageUri", userImageUri);*/
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
                    Intent home = new Intent(LocateFriendsActivity.this, MainActivity.class);

                    home.putExtra("userId", currentUser);
                    home.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    home.putStringArrayListExtra("friendsList", friendsList);
                    home.putStringArrayListExtra("eventInfo", eventInfo);
                    home.putStringArrayListExtra("eventTitles", eventTitles);
                    home.putStringArrayListExtra("eventLocation", eventLocation);

                    android.app.Fragment MainActivityFrag = new android.app.Fragment();
                    Bundle MainActivityBundle = new Bundle();
                    MainActivityBundle.putString("currentUser", currentUser);
                    MainActivityBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    MainActivityBundle.putStringArrayList("eventInfo", eventInfo);
                    MainActivityBundle.putStringArrayList("eventTitles", eventTitles);
                    MainActivityBundle.putStringArrayList("eventLocation", eventLocation);
                    MainActivityBundle.putStringArrayList("friendsList", friendsList);
                    MainActivityFrag.setArguments(MainActivityBundle);

                    startActivity(home);
                    break;
                /*Case 1 used for the FindPeople item in the list and redirects the user to the
                 *locate friends activity page.
                 */
                case 1:
                    Intent FindPeople = new Intent(LocateFriendsActivity.this, LocateFriendsActivity.class);

                    /*startActivity(FindPeople);*/
                    break;
                /*Case 2 used for the FindEvents item in the list and redirects the user to the
                *locate event activity page.
                */
                case 2:
                    Intent Events = new Intent(LocateFriendsActivity.this, EventsActivity.class);

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
                    Intent EditProfile = new Intent(LocateFriendsActivity.this, EditProfileActivity.class);

                    EditProfile.putExtra("userId", currentUser);
                    EditProfile.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    EditProfile.putStringArrayListExtra("eventInfo", eventInfo);
                    EditProfile.putStringArrayListExtra("friendsList", friendsList);
                    EditProfile.putStringArrayListExtra("eventTitles", eventTitles);
                    EditProfile.putStringArrayListExtra("eventLocation", eventLocation);

                    android.app.Fragment EditProfileFrag = new android.app.Fragment();
                    Bundle EditProfileBundle = new Bundle();
                    EditProfileBundle.putString("currentUser", currentUser);
                    EditProfileBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    EditProfileBundle.putStringArrayList("eventInfo", eventInfo);
                    EditProfileBundle.putStringArrayList("friendsList", friendsList);
                    EditProfileBundle.putStringArrayList("eventTitles", eventTitles);
                    EditProfileBundle.putStringArrayList("eventLocation", eventLocation);
                    EditProfileFrag.setArguments(EditProfileBundle);
                    startActivity(EditProfile);
                    break;
                /*Case 4 used for the Edit Profile item in the list and redirects the user to the
                *profile activity page.
                */
                case 4:
                    Intent Settings = new Intent(LocateFriendsActivity.this, SettingsActivity.class);

                    Settings.putExtra("userId", currentUser);
                    Settings.putStringArrayListExtra("eventInfo", eventInfo);
                    Settings.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    Settings.putStringArrayListExtra("friendsList", friendsList);
                    Settings.putStringArrayListExtra("eventTitles", eventTitles);
                    Settings.putStringArrayListExtra("eventLocation", eventLocation);

                    android.app.Fragment SettingsFrag = new android.app.Fragment();
                    Bundle SettingsBundle = new Bundle();
                    SettingsBundle.putString("currentUser", currentUser);
                    SettingsBundle.putStringArrayList("eventInfo", eventInfo);
                    SettingsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    SettingsBundle.putStringArrayList("friendsList", friendsList);
                    SettingsBundle.putStringArrayList("eventTitles", eventTitles);
                    SettingsBundle.putStringArrayList("eventLocation", eventLocation);
                    SettingsFrag.setArguments(SettingsBundle);

                    startActivity(Settings);
                    break;

                /*Case 5 used for the Settings item in the list and redirects the user to the
                 *settings activity page.
                 */
                case 5:
                    Intent main = new Intent(LocateFriendsActivity.this, Login_Screen.class);
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




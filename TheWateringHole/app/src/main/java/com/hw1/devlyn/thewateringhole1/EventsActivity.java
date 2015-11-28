package com.hw1.devlyn.thewateringhole1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.info.wateringhole.slidingmenu.adapter.NavDrawerListAdapter;
import info.info.wateringhole.slidingmenu.model.NavDrawerItem;
/*EventsActivity used so the user can create an event from within the activity.*/
public class EventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /*Fields used to store the values from the intent that is passed in from MainActivity.*/
    private String currentUser;
    private String idUserProfile;
    private String description;
    private String likes_dislikes;
    private String userName;
    private String events;
    private ArrayList<String> eventInfo;
    private ArrayList<String> eventTitles;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> friendsList;
    private ArrayList<String> eventLocation;
    private String eventDesc;
    private String numPart;
    private String eventName;
    private double userLongitude;
    private double userLatitude;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView eventsList;
    private ListView eventInfoList;
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

    /*onCreate method that uses and intent, fragment and bundle to pass the values from the user
      back and forth through this activity and others that are connected.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        /*Intent created to store the values that are retrieved from the MainActivity.*/
        Intent thisIntent = getIntent();
            currentUser = thisIntent.getStringExtra("userId");
            userLongitude = thisIntent.getDoubleExtra("userLongitude", userLongitude);
            userLatitude = thisIntent.getDoubleExtra("userLatitude", userLatitude);
            eventInfo = thisIntent.getStringArrayListExtra("eventInfo");
            eventTitles = thisIntent.getStringArrayListExtra("eventTitles");
            eventLocation = thisIntent.getStringArrayListExtra("eventLocation");
            userProfileInfo = thisIntent.getStringArrayListExtra("userProfileInfo");
            friendsList = thisIntent.getStringArrayListExtra("friendsList");


        /*This is creating a Fragment object where a bundle is created to store the values of the
            data. This fragment can then be accessed from within FragmentEditProfile to retrieve the
            data and use it throughout the activity.
         */
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
            bundle.putStringArrayList("eventInfo", eventInfo);
            bundle.putStringArrayList("eventTitles", eventTitles);
            bundle.putStringArrayList("eventLocation", eventLocation);
            bundle.putStringArrayList("userProfileInfo", userProfileInfo);
            bundle.putStringArrayList("friendsList", friendsList);
        fragment.setArguments(bundle);

        eventsList = (ListView) findViewById(R.id.eventsList);
        eventsList.setOnItemClickListener(this);


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        for(int i = 0 ; i < eventTitles.size(); i = i + 1) {
            //for(int j = 0; j < arrayAdapter.getCount(); j++) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        eventTitles);
                eventsList.setAdapter(arrayAdapter);
                //i++;
            //}
        }

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
        // Find Hangouts
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        // Edit Profile
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        // Settings
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CharSequence[] eventInfo1 = {eventInfo.get(1), eventInfo.get(2)};//eventInfo.toArray(new CharSequence[eventInfo.size()]);
        CharSequence[] eventInfo2 = {eventInfo.get(4), eventInfo.get(5)};
        CharSequence[] eventInfo3 = {eventInfo.get(7), eventInfo.get(8)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id == 0) {
            String title = eventTitles.get(0);
            builder.setTitle(title);
            builder.setItems(eventInfo1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (eventTitles.get(0) != null) {
                        Intent LocateEvents = new Intent(EventsActivity.this, LocateEventsActivity.class);
                            LocateEvents.putStringArrayListExtra("eventInfo", eventInfo);
                            LocateEvents.putStringArrayListExtra("eventTitles", eventTitles);
                            LocateEvents.putStringArrayListExtra("eventLocation", eventLocation);
                        startActivity(LocateEvents);
                    }
                }
            });
        } else if (id == 1) {
            String title = eventTitles.get(1);
            builder.setTitle(title);
            builder.setItems(eventInfo2, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if(eventTitles.get(1) != null) {
                        Intent LocateEvents = new Intent(EventsActivity.this, LocateEventsActivity.class);
                        LocateEvents.putStringArrayListExtra("eventInfo", eventInfo);
                        LocateEvents.putStringArrayListExtra("eventTitles", eventTitles);
                        startActivity(LocateEvents);
                    }
                    // Do something with the selection
                }
            });
        } else if (id == 2) {
            String title = eventTitles.get(2);
            builder.setTitle(title);
            builder.setItems(eventInfo3, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if(eventTitles.get(2) != null) {
                        Intent LocateEvents = new Intent(EventsActivity.this, LocateEventsActivity.class);
                        LocateEvents.putStringArrayListExtra("eventInfo", eventInfo);
                        LocateEvents.putStringArrayListExtra("eventTitles", eventTitles);
                        LocateEvents.putStringArrayListExtra("eventLocation", eventLocation);
                        startActivity(LocateEvents);
                    }
                    // Do something with the selection
                }
            });
        }
 /*       //builder.setTitle(String.valueOf(id));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
            }
        });*/
        AlertDialog alert = builder.create();
        alert.show();
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
                    Intent home = new Intent(EventsActivity.this, MainActivity.class);
                    home.putExtra("userId", currentUser);
                    home.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    home.putStringArrayListExtra("friendsList", friendsList);
                    home.putStringArrayListExtra("eventInfo", eventInfo);
                    home.putExtra("userLongitude", userLongitude);
                    home.putExtra("userLatitude", userLatitude);

                    android.app.Fragment MainActivityFrag = new android.app.Fragment();
                    Bundle MainActivityBundle = new Bundle();
                    MainActivityBundle.putString("currentUser", currentUser);
                    MainActivityBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    MainActivityBundle.putStringArrayList("eventInfo", eventInfo);
                    MainActivityBundle.putStringArrayList("friendsList", friendsList);
                    MainActivityFrag.setArguments(MainActivityBundle);

                    startActivity(home);
                    break;
                /*Case 1 used for the FindPeople item in the list and redirects the user to the
                 *locate friends activity page.
                 */
                case 1:
                    Intent FindPeople = new Intent(EventsActivity.this, LocateFriendsActivity.class);

                    FindPeople.putExtra("userId", currentUser);
                    FindPeople.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    FindPeople.putStringArrayListExtra("friendsList", friendsList);
                    FindPeople.putStringArrayListExtra("eventInfo", eventInfo);
                    FindPeople.putExtra("userLongitude", userLongitude);
                    FindPeople.putExtra("userLatitude", userLatitude);
                    //FindPeople.putExtra("userImageUri", userImageUri);*/

                    android.app.Fragment FindPeopleFrag = new android.app.Fragment();
                    Bundle FindPeopleBundle = new Bundle();
                    FindPeopleBundle.putString("currentUser", currentUser);
                    FindPeopleBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    FindPeopleBundle.putStringArrayList("friendsList", friendsList);
                    FindPeopleBundle.putStringArrayList("eventInfo", eventInfo);
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
                    Intent Events = new Intent(EventsActivity.this, EventsActivity.class);

                    /*FindEvents.putExtra("userId", currentUser);
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
                    FindEventsFrag.setArguments(FindEventsBundle);*/

                    //startActivity(Events);
                    break;
                /*Case 3 used for the FindHangouts item in the list and redirects the user to the
                 *locate hangouts activity page.
                 */
                case 3:
                    Intent FindHangouts = new Intent(EventsActivity.this, LocateHangoutActivity.class);

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
                    Intent EditProfile = new Intent(EventsActivity.this, EditProfileActivity.class);

                    EditProfile.putExtra("userId", currentUser);
                    EditProfile.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    EditProfile.putStringArrayListExtra("eventInfo", eventInfo);
                    EditProfile.putStringArrayListExtra("friendsList", friendsList);
                    EditProfile.putExtra("userLongitude", userLongitude);
                    EditProfile.putExtra("userLatitude", userLatitude);

                    android.app.Fragment EditProfileFrag = new android.app.Fragment();
                    Bundle EditProfileBundle = new Bundle();
                    EditProfileBundle.putString("currentUser", currentUser);
                    EditProfileBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    EditProfileBundle.putStringArrayList("eventInfo", eventInfo);
                    EditProfileBundle.putStringArrayList("friendsList", friendsList);
                    EditProfileBundle.putDouble("userLongitude", userLongitude);
                    EditProfileBundle.putDouble("userLatitude", userLatitude);
                    EditProfileFrag.setArguments(EditProfileBundle);

                    startActivity(EditProfile);
                    break;
                /*Case 5 used for the Settings item in the list and redirects the user to the
                 *settings activity page.
                 */
                case 5:
                    Intent Settings = new Intent(EventsActivity.this, SettingsActivity.class);

                    Settings.putExtra("userId", currentUser);
                    Settings.putStringArrayListExtra("eventInfo", eventInfo);
                    Settings.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    Settings.putStringArrayListExtra("friendsList", friendsList);
                    Settings.putExtra("userLongitude", userLongitude);
                    Settings.putExtra("userLatitude", userLatitude);

                    android.app.Fragment SettingsFrag = new android.app.Fragment();
                    Bundle SettingsBundle = new Bundle();
                    SettingsBundle.putString("currentUser", currentUser);
                    SettingsBundle.putStringArrayList("eventInfo", eventInfo);
                    SettingsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    SettingsBundle.putStringArrayList("friendsList", friendsList);
                    SettingsBundle.putDouble("userLongitude", userLongitude);
                    SettingsBundle.putDouble("userLatitude", userLatitude);
                    SettingsFrag.setArguments(SettingsBundle);

                    startActivity(Settings);
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


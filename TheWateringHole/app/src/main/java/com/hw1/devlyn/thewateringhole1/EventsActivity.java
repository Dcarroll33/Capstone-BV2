package com.hw1.devlyn.thewateringhole1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import info.info.wateringhole.slidingmenu.adapter.NavDrawerListAdapter;
import info.info.wateringhole.slidingmenu.model.NavDrawerItem;
/**
 * EventsActivity used so the user can create an event from within the activity.
 */
public class EventsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * Global variables used to store the values from the intent that is passed in from MainActivity.
     */
    /**
     * Global string currentUser used to store the value of the currentUser passed in from the
     * intent.
     */
    private String currentUser;
    private String idUserProfile;
    private String description;
    private String likes_dislikes;
    private String userName;
    private String events;
    /**
     * Global ArrayList of strings eventInfo used to store the values eventName, number of people
     * participating and description of the event.
     */
    private ArrayList<String> eventInfo;

    /**
     * Global ArrayList of strings eventTitles used to store the values of all eventNames.
     */
    private ArrayList<String> eventTitles;

    /**
     * Global ArrayList of strings userProfileInfo used to store the values userId, profileId,
     * userName, description, events, likes, dislikes and image uri.
     */
    private ArrayList<String> userProfileInfo;

    /**
     * Global ArrayList of strings friendsList used to store the friends to the user and their
     * location.
     */
    private ArrayList<String> friendsList;

    /**
     * Global ArrayList of strings eventLocation used to store the eventName, eventLatitude and
     * eventLongitude.
     */
    private ArrayList<String> eventLocation;

    private String eventDesc;
    private String numPart;
    private String eventName;

    private double userLongitude;
    private double userLatitude;

    /**
     * Global DrawerLayout mDrawerLayout used to assign the sliding menu layout.
     */
    private DrawerLayout mDrawerLayout;

    /**
     * Global ListView mDrawerList used to store the sliding menu items and icons.
     */
    private ListView mDrawerList;

    /**
     * Global ListView eventsList used to store the event information.
     */
    private ListView eventsList;
    private ListView eventInfoList;

    /**
     * Global ActionBarDrawerToggle mDrawerToggle is used as the sliding menu toggle button.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    /**
     * Global CharSequence mDrawerTitle used to store the sliding menu title.
     */
    private CharSequence mDrawerTitle;

    /**
     *Global CharSequence mTitle used to store app title.
     */
    private CharSequence mTitle;

    /**
     * Global String array navMenuTitles used to store the menu titles.
     */
    private String[] navMenuTitles;

    /**
     * Global TypedArray navMenuIcons used to store slide menu icons.
     */
    private TypedArray navMenuIcons;

    /**
     * Global ArrayList<NavDrawerItem> navDrawerItems used to store the sliding menu items.
     */
    private ArrayList<NavDrawerItem> navDrawerItems;

    /**
     * Global NavDrawerListAdapter adapter used with the sliding menu methods.
     */
    private NavDrawerListAdapter adapter;


    Button locate;

    /**
     * onCreate method that uses an intent and bundle to pass the values from the user
     * back and forth through this activity, other activities and fragments that are connected to
     * this activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        /**
         * Intent created to store the values that are retrieved from the MainActivity.
         */
        Intent thisIntent = getIntent();
            currentUser = thisIntent.getStringExtra("userId");
            userLongitude = thisIntent.getDoubleExtra("userLongitude", userLongitude);
            userLatitude = thisIntent.getDoubleExtra("userLatitude", userLatitude);
            eventInfo = thisIntent.getStringArrayListExtra("eventInfo");
            eventTitles = thisIntent.getStringArrayListExtra("eventTitles");
            eventLocation = thisIntent.getStringArrayListExtra("eventLocation");
            userProfileInfo = thisIntent.getStringArrayListExtra("userProfileInfo");
            friendsList = thisIntent.getStringArrayListExtra("friendsList");


        /**
         * This is creating a Fragment object where a bundle is created to store the values of the
         * data. This fragment can then be accessed from within FragmentEditProfile to retrieve the
         * data and use it throughout the activity.
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

        /**
         * Initializing eventsList to the resource ListView eventsList.
         */
        eventsList = (ListView) findViewById(R.id.eventsList);

        /**
         * Setting the onItemClickListener for eventsList.
         */
        eventsList.setOnItemClickListener(this);


        /** This is the array adapter, it takes the context of the activity as a
         *  first parameter, the type of list view as a second parameter and your
         *  array as a third parameter. The for loop iterates through the eventTitles ArrayList size
         *  and sets the eventTitles as items in the eventList.
         */
        for(int i = 0 ; i < eventTitles.size(); i = i + 1) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        this,
                        R.layout.activity_events_listview_text_color,R.id.list_content,
                        eventTitles);
                eventsList.setAdapter(arrayAdapter);

        }

        /**
         * Assigning mTitle and mDrawerTitle to the result of the method getTitle().
         */
        mTitle = mDrawerTitle = getTitle();

        /**
         * Loading slider menu items and assigning them to the navMenuTitles.
         */
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        /**
         * Loading nav drawer icons from resources and assigning them to the navMenuIcons.
         */
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        /**
         * Initializing the mDrawerLayout with the layout from resources drawer_layout.
         */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**
         * Initializing the mDrawerList with the ListView from resources list_slider_menu.
         */
        mDrawerList = (ListView) findViewById(R.id.list_slider_menu);

        Log.d("Main Page", "mDrawerList created" + mDrawerList);

        /**
         * Initializing the navDrawerItems to a new ArrayList<NavDrawerItem>.
         */
        navDrawerItems = new ArrayList<NavDrawerItem>();

        /**
         * Adding nav drawer items (navMenuTitles and navMenuIcons to ArrayList navDrawerItems.
         *
         *
         * Home item in the sliding menu.
         */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));

        /**
         * Find People item in the sliding menu.
         */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));

        /**
         * Find Events item in the sliding menu.
         */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));

        /**
         * Edit Profile item in the sliding menu.
         */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));

        /**
         * Settings item in the sliding menu.
         */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));

        /**
         * Sign out item in the sliding menu.
         */
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));

        /**
         * Recycle the typed array.
         */
        navMenuIcons.recycle();

        /**
         * Setting the onItemClickListener with new SlidMenuClickListener() to mDrawerList.
         */
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        /**
         * Assigning the sliding menu list adapter to adapter.
         */
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);

        /**
         * Setting the adapter to the mDrawerList.
         */
        mDrawerList.setAdapter(adapter);

        /**
         * Android action bar options to set the home display and enable the sliding menu home
         * button.
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /**
         * Enabling action bar app icon and enabling it to behave as a toggle button.
         */
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name) // nav drawer close - description for accessibility

        {
            /**
             * onDrawerClosed method used to reset the title and show the action bar icons.
             * @param view
             */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            /**
             * onDrawerOpened method used to reset the title and hide the action bar icons.
             * @param drawerView
             */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        /**
         * Setting the drawerListener to the mDrawerLayout.
         */
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /**
         * For the first time display view the first sliding menu item.
         */
        if (savedInstanceState == null) {
            displayView(0);
        }
    }

    /**
     * onItemClick method that detects what item in the ListView is selected and depending on which
     * item is selected determines what values are populated in the alertDialog pop up.
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final int itemSelection = (int) id;

        /**
         * Assigns the values that are returned from the eventInfo ArrayList to CharSequence arrays.
         */
        CharSequence[] eventInfo1 = {eventInfo.get(1), eventInfo.get(2)};//eventInfo.toArray(new CharSequence[eventInfo.size()]);
        CharSequence[] eventInfo2 = {eventInfo.get(4), eventInfo.get(5)};
        CharSequence[] eventInfo3 = {eventInfo.get(7), eventInfo.get(8)};

        /**
         * Initializes a new alert dialog builder.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id == 0) {
            /**
             * Assigns and sets the titles of the events.
             */
            String title = eventTitles.get(0);
            builder.setTitle(title);

            /**
             * Setting items in the builder based off the eventInfo ArrayList and values from the
             * DialogInterface.OnClickListener().
             */
            builder.setItems(eventInfo1, new DialogInterface.OnClickListener() {
                /**
                 * onClick method that checks to see what item was selected. Then it creates an
                 * intent that passes the eventInfo, eventTitles and eventLocation to the
                 * LocateEventsActivity. Then the intent is fired.
                 * @param dialog
                 * @param item
                 */
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
                        LocateEvents.putStringArrayListExtra("eventLocation", eventLocation);
                        startActivity(LocateEvents);
                    }
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
                }
            });
        }
        /*final int test = itemSelection;
        final int test2 = (int) id;
        String title = eventInfo.get(test);
        builder.setTitle(title);
        builder.setItems(eventInfoResult, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(eventInfo.get(test) != null) {
                    Intent LocateEvents = new Intent(EventsActivity.this, LocateEventsActivity.class);
                    LocateEvents.putStringArrayListExtra("eventInfo", eventInfo);
                    LocateEvents.putStringArrayListExtra("eventTitles", eventTitles);
                    LocateEvents.putStringArrayListExtra("eventLocation", eventLocation);
                    startActivity(LocateEvents);
                }
                // Do something with the selection
            }
        });*/
 /*       //builder.setTitle(String.valueOf(id));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
            }
        });*/

        /**
         * Creates and assigns the newly created AlertDialog builder to AlertDialog alert.
         */
        AlertDialog alert = builder.create();

        /**
         * Displays the AlertDialog pop up with the eventInfo.
         */
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_slide_out_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Toggle nav drawer on selecting action bar app icon/title.
         */
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        /**
         * Switch statement to handle action bar actions click
         */
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

    /**
     * Setter method for the app title.
     * @param title
     */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * onPostCreate method that syncs the toggle state after onRestoreInstanceState has occurred.
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * onConfigurationChanged method to pass any configuration change to the drawer toggle.
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Slide menu item click listener private class that checks for item clicks within the sliding
     * menu. Then using onItemClick the item position is detected and passed to a switch statement.
     * Each case in the switch statement creates an intent and bundle to be passed to the activity
     * or fragment that is called.
     * */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            /**
             * display view for selected nav drawer item
             */
            displayView(position);


            switch (position) {
                /**
                 * Case 0 used for the home item in the list and redirects the user to the main
                 * activity page as well as creates the intent and bundle needed to be passed.
                 */
                case 0:
                    Intent home = new Intent(EventsActivity.this, MainActivity.class);
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
                    MainActivityBundle.putStringArrayList("eventTitles", eventTitles);
                    MainActivityBundle.putStringArrayList("eventLocation", eventLocation);
                    MainActivityBundle.putStringArrayList("friendsList", friendsList);
                    MainActivityFrag.setArguments(MainActivityBundle);

                    startActivity(home);
                    break;
                /**
                 * Case 1 used for the FindPeople item in the list and redirects the user to the
                 * locate friends activity page as well as creates the intent and bundle needed to
                 * be passed.
                 */
                case 1:
                    Intent FindPeople = new Intent(EventsActivity.this, LocateFriendsActivity.class);

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
                /**
                 * Case 2 used for the FindEvents item in the list and redirects the user to the
                 *locate event activity page as well as creates the intent and bundle needed to be
                 * passed.
                 */
                case 2:
                    Intent Events = new Intent(EventsActivity.this, EventsActivity.class);

                    //startActivity(Events);
                    break;
                /**
                 * Case 3 used for the EditProfile item in the list and redirects the user to the
                 * edit profile page as well as creates the intent and bundle needed to be passed.
                 */
                case 3:
                    Intent EditProfile = new Intent(EventsActivity.this, EditProfileActivity.class);

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
                    EditProfileBundle.putDouble("userLongitude", userLongitude);
                    EditProfileBundle.putDouble("userLatitude", userLatitude);
                    EditProfileFrag.setArguments(EditProfileBundle);

                    startActivity(EditProfile);
                    break;
                /**
                 * Case 4 used for the Settings item in the list and redirects the user to the
                 * settings page as well as creates the intent and bundle needed to be passed.
                 */
                case 4:

                Intent Settings = new Intent(EventsActivity.this, SettingsActivity.class);

                Settings.putExtra("userId", currentUser);
                Settings.putStringArrayListExtra("eventInfo", eventInfo);
                Settings.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                Settings.putStringArrayListExtra("friendsList", friendsList);
                Settings.putStringArrayListExtra("eventTitles", eventTitles);
                Settings.putStringArrayListExtra("eventLocation", eventLocation);
                Settings.putExtra("userLongitude", userLongitude);
                Settings.putExtra("userLatitude", userLatitude);

                android.app.Fragment SettingsFrag = new android.app.Fragment();
                Bundle SettingsBundle = new Bundle();
                SettingsBundle.putString("currentUser", currentUser);
                SettingsBundle.putStringArrayList("eventInfo", eventInfo);
                SettingsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                SettingsBundle.putStringArrayList("friendsList", friendsList);
                SettingsBundle.putStringArrayList("eventTitles", eventTitles);
                SettingsBundle.putStringArrayList("eventLocation", eventLocation);
                SettingsBundle.putDouble("userLongitude", userLongitude);
                SettingsBundle.putDouble("userLatitude", userLatitude);
                SettingsFrag.setArguments(SettingsBundle);

                startActivity(Settings);
                break;
                /**
                 * Case 5 used for the Sign out item in the list and redirects the user to the
                 * login activity page.
                 */
                case 5:

                    Intent main = new Intent(EventsActivity.this, Login_Screen.class);
                    startActivity(main);
                    break;
                default:

            }
        }
    }
    /**
     * Displaying fragment view for selected nav drawer list item. Then update selected item and
     * title and close the sliding menu.
     */
    private void displayView(int position) {
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}


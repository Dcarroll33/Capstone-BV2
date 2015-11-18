package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import info.info.wateringhole.slidingmenu.adapter.NavDrawerListAdapter;
import info.info.wateringhole.slidingmenu.model.NavDrawerItem;

/*This is the EditProfileActivity that is the base activity for the FragmentEditProfileActivity. The
    navigation drawer for this activity is constructed within this class, as well as the fragment and
    bundle that pass the information to the FragmentEditProfileActivity.
 */
public class EditProfileActivity extends AppCompatActivity {
    /*Field for the navigation drawer layout.*/
    private DrawerLayout mDrawerLayout;
    /*Field for the listView that displays the items within the navigation drawer.*/
    private ListView mDrawerList;
    /*Field for the navigation drawer button to open and close the navigation drawer.*/
    private ActionBarDrawerToggle mDrawerToggle;

    /*Field for the navigation drawer title.*/
    private CharSequence mDrawerTitle;

    /*Field to store the app title.*/
    private CharSequence mTitle;

    /*Field for navigation drawer menu items.*/
    private String[] navMenuTitles;

    /*Field for the icons that are used for the menu items.*/
    private TypedArray navMenuIcons;

    /*Field for the navigation drawer items that are in an ArrayList.*/
    private ArrayList<NavDrawerItem> navDrawerItems;

    /*Field for the navigation drawer adapter.*/
    private NavDrawerListAdapter adapter;

    /*Field for the REQUEST_CODE needed to access the picture gallery.*/
    private static final int REQUEST_CODE = 1;

    /*Field for the bitmap that is needed to grab the image data from the picture gallery.*/
    private Bitmap bitmap;

    /*EditTexts that are within the EditProfileActivity layout that display description,
        likes/dislikes and username.
     */
    private EditText Description;
    private EditText Likes_Dislikes;
    private EditText UserName;

    /*Buttons that are within the EditProfileActivity layout.*/
    private Button Save;

    /*Strings that are needed to assign the values from the bundle that is being passed to the
        EditProfileActivity.
     */
    private String currentUser;
    private String userImageUri;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> eventInfo;
    private ArrayList<String> friendsList;

    /*Doubles that are needed for the user's longitude and latitude.*/
    private double userLongitude;
    private double userLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        /*This is an intent used to receive the information that is being passed from an intent in
            FragmentMainActivity to this activity.
         */
        Intent thisIntent = getIntent();
        Log.d("MainAct", "Inside editProfile");
            currentUser = thisIntent.getStringExtra("userId");
            userProfileInfo = thisIntent.getStringArrayListExtra("userProfileInfo");
            eventInfo = thisIntent.getStringArrayListExtra("eventInfo");
            friendsList = thisIntent.getStringArrayListExtra("friendsList");
            userLongitude = thisIntent.getDoubleExtra("userLongitude", userLongitude);
            userLatitude = thisIntent.getDoubleExtra("userLatitude", userLatitude);
            //userImageUri = thisIntent.getStringExtra("userImageUri");

        /*This is creating a Fragment object where a bundle is created to store the values of the
            data. This fragment can then be accessed from within FragmentEditProfile to retrieve the
            data and use it throughout the activity.
         */
        Fragment fragment = new Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("currentUser", currentUser);
            bundle.putStringArrayList("userProfileInfo", userProfileInfo);
            bundle.putDouble("userLongitude", userLongitude);
            bundle.putDouble("userLatitude", userLatitude);
            //bundle.putString("userImageUri", userImageUri);
        fragment.setArguments(bundle);

        /*This is where the EditTexts that are within the FragmentEditProfile are located and
            assigned to the the fields that were set up earlier.
          */
        Description = (EditText) this.findViewById(R.id.Description);
        Likes_Dislikes = (EditText) this.findViewById(R.id.Likes_Dislikes);
        UserName = (EditText) this.findViewById(R.id.Username);

        Save = (Button) this.findViewById(R.id.save_btn);

        /*Save.setOnClickListener((View.OnClickListener) Save);*/


        mTitle = mDrawerTitle = getTitle();
        getSupportActionBar().setTitle(mTitle);

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
                    Intent home = new Intent(EditProfileActivity.this, MainActivity.class);

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
                    Intent FindPeople = new Intent(EditProfileActivity.this, LocateFriendsActivity.class);

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
                    Intent Events = new Intent(EditProfileActivity.this, EventsActivity.class);

                    Events.putExtra("userId", currentUser);
                    Events.putStringArrayListExtra("eventInfo", eventInfo);
                    Events.putStringArrayListExtra("userProfileInfo", userProfileInfo);
                    Events.putStringArrayListExtra("friendsList", friendsList);


                    android.app.Fragment FindEventsFrag = new android.app.Fragment();
                    Bundle FindEventsBundle = new Bundle();
                    FindEventsBundle.putString("currentUser", currentUser);
                    FindEventsBundle.putStringArrayList("eventInfo", eventInfo);
                    FindEventsBundle.putStringArrayList("userProfileInfo", userProfileInfo);
                    FindEventsBundle.putStringArrayList("friendsList", friendsList);
                    FindEventsFrag.setArguments(FindEventsBundle);

                    startActivity(Events);
                    break;
                /*Case 3 used for the FindHangouts item in the list and redirects the user to the
                 *locate hangouts activity page.
                 */
                case 3:
                    Intent FindHangouts = new Intent(EditProfileActivity.this, LocateHangoutActivity.class);

                    startActivity(FindHangouts);
                    break;
                /*Case 4 used for the Edit Profile item in the list and redirects the user to the
                *profile activity page.
                */
                case 4:
                    Intent EditProfile = new Intent(EditProfileActivity.this, EditProfileActivity.class);

                    /*startActivity(EditProfile);*/
                    break;

                /*Case 5 used for the Settings item in the list and redirects the user to the
                 *settings activity page.
                 */
                case 5:
                    Intent Settings = new Intent(EditProfileActivity.this, SettingsActivity.class);

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
     * Displaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        setTitle(navMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

}


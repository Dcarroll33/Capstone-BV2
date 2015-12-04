package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FragmentLocateFriends extends Fragment implements  View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private MapView mMapView;
    private GoogleMap googleMap;

    private String currentUser;
    private String idUserProfile;
    private String userName;
    private String description;
    private String events;
    private String likes_dislikes;
    private double userLongitude;
    private double userLatitude;
    private ArrayList<String> friendsList;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> eventInfo;
    private ArrayList<String> eventTitles;
    private ArrayList<String> eventLocation;
    private double currentLongitude;
    private double currentLatitude;
    private double updatedLongitude;
    private double updatedLatitude;
    private Marker userLocationMarker;
    private Marker friendLocationMarker;
    private Marker userLocationMarker2;

    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 5000; // 5 sec
    private static int FATEST_INTERVAL = 1000; // 1 sec
    private static int DISPLACEMENT = 1; // 1 meters

    ConnectDb conDb = new ConnectDb();

    /*Fields for the buttons to be used in this class.*/
    Button FriendsProfile;
    Button AddFriend;


    /**
     * Method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEvents.
     */
    public static FragmentLocateFriends newInstance(String param1, String param2) {
        FragmentLocateFriends fragment = new FragmentLocateFriends();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentLocateFriends() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getActivity().getIntent().getExtras();
        currentUser = args.getString("userId", currentUser);
        userLongitude = args.getDouble("userLongitude", userLongitude);
        userLatitude = args.getDouble("userLatitude", userLatitude);
        friendsList = args.getStringArrayList("friendsList");
        userProfileInfo = args.getStringArrayList("userProfileInfo");
        eventInfo = args.getStringArrayList("eventInfo");
        eventTitles = args.getStringArrayList("eventTitles");
        eventLocation = args.getStringArrayList("eventLocation");

        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater
                .inflate(R.layout.fragment_locate_friends, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        setUpMapIfNeeded(rootView);

        googleMap = mMapView.getMap();
        MapsInitializer.initialize(this.getActivity());

        getButtons(rootView);

        FriendsProfile = (Button) rootView.findViewById(R.id.friends_profile_btn);
        AddFriend = (Button) rootView.findViewById(R.id.addFriend);

        userName = userProfileInfo.get(2);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        stopLocationUpdates();
    }

    /**
     * Method to display the location on UI
     * */
    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);


        if (mLastLocation != null) {

            //Send updated user location data to server
            try {
                String[] currentLocation = {"currentLocation", currentUser, String.valueOf(mLastLocation.getLongitude()), String.valueOf(mLastLocation.getLatitude())};
                conDb.execute(currentLocation).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            ArrayList<Double> currentLocation = conDb.setCurrentCoords();
            if (currentLocation != null) {
                Toast.makeText(getActivity(), "User Location Updated", Toast.LENGTH_SHORT).show();
            } else if (currentLocation == null) {
                Toast.makeText(getActivity(), "Location Update Failed", Toast.LENGTH_SHORT).show();

            }

            updatedLatitude = mLastLocation.getLatitude();
            updatedLongitude = mLastLocation.getLongitude();
            LatLng userLocation = new LatLng(updatedLatitude,updatedLongitude);
            userLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .position(new LatLng(updatedLatitude, updatedLongitude)).title(userName));
            if (userLocationMarker != null) {
                userLocationMarker.setPosition(userLocation);
            }

            Toast.makeText(getActivity(), "CurrentCoords: " + updatedLatitude + "," + updatedLongitude, Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getActivity(), "Location update failed", Toast.LENGTH_SHORT).show();
        }
//        userLocationMarker.remove();
        //currentLocationUpdate();
        setUpMapIfNeeded(mMapView);

    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                //finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getActivity(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        //userLocationMarker.remove();
        displayLocation();



        //this.mMapView.invalidate();
    }

    public void currentLocationUpdate(){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18.0f));
        if (userLocationMarker != null) {
            userLocationMarker.remove();
            userLocationMarker2 = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title(userName));
        }
    }


    private void setUpMapIfNeeded(View rootView) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            googleMap = ((MapView) rootView.findViewById(R.id.mapview)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        } else if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            //userLocationMarker.remove();
            currentLocationUpdate();
            //googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                //@Override
                //public void onMyLocationChange(Location arg0) {

                    //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 18.0f));
/*
                    if (userLocationMarker != null) {
                        userLocationMarker.remove();
                        userLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .position(new LatLng(updatedLatitude, updatedLongitude)).title(userName));
                    }*/

                        /*For loop to skip to the next row in friends table in database. The i + 3 pulls the friendsName, friendsLongitude and friendLatitude from the friends table.
                           Markers are placed at each friends location until there are no more friends in the table.
                        */
                    for (int i = 0; i < friendsList.size(); i = i + 3) {
                        friendLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                .position(new LatLng(Double.valueOf(friendsList.get(i + 2)), Double.valueOf(friendsList.get(i + 1)))).title(friendsList.get(i)));
                    }

                    //currentLongitude = updatedLongitude;
                    //currentLatitude = updatedLatitude;

                    /*try {
                        String[] currentLocation = {"currentLocation", currentUser, String.valueOf(currentLongitude), String.valueOf(currentLatitude)};
                        conDb.execute(currentLocation).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Double> currentLocation = conDb.setCurrentCoords();
                    if (currentLocation != null) {
                        Toast.makeText(getActivity(), "User Location Updated", Toast.LENGTH_SHORT).show();
                    } else if (currentLocation == null) {
                        Toast.makeText(getActivity(), "Location Update Failed", Toast.LENGTH_SHORT).show();
                    }*/

                    //googleMap.setOnMyLocationChangeListener(null);
                }
            }





    public void getButtons(View v){
        if(v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i <vg.getChildCount(); i++) {
                View v1 = vg.getChildAt(i);
                if (v1 instanceof Button) {
                    Button b = (Button) v1;
                    b.setOnClickListener(this);
                }
                else if(v1 instanceof ViewGroup) {
                    getButtons(v1);
                }
            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /*This method is for the on screen clicks by the user depending on which button is pushed
        in this case the Events, Friends, Profile or Settings buttons. Once one button has been
        clicked depending on their relationship the screen will switch to the appropriate screen.*/
    @Override
    public void onClick(View view) {
        if (view == FriendsProfile) {
            Intent friendsProfile = new Intent(getActivity(), FriendsProfileActivity.class);
            friendsProfile.putExtra("userId", currentUser);
            friendsProfile.putExtra("idUserProfile", idUserProfile);
            friendsProfile.putExtra("userName", userName);
            friendsProfile.putExtra("description", "" + description);
            friendsProfile.putExtra("events", events);
            friendsProfile.putExtra("likes_dislikes", likes_dislikes);
            friendsProfile.putExtra("userLongitude", userLongitude);
            friendsProfile.putExtra("userLatitude", userLatitude);

            android.app.Fragment fragment = new android.app.Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("currentUser", currentUser);
            bundle.putString("idUserProfile", idUserProfile);
            bundle.putString("userName", userName);
            bundle.putString("description", description);
            bundle.putString("events", events);
            bundle.putString("likes_dislikes", likes_dislikes);
            bundle.putDouble("userLongitude", userLongitude);
            bundle.putDouble("userLatitude", userLatitude);
            //bundle.putString("userImageUri", userImageUri);
            fragment.setArguments(bundle);

            Button b = (Button) view;
            this.startActivity(friendsProfile);
        }
        if (view == AddFriend) {
            Intent addFriend = new Intent(getActivity(), FriendsActivity.class);
            addFriend.putExtra("userId", currentUser);
            addFriend.putStringArrayListExtra("userProfileInfo", userProfileInfo);
            addFriend.putStringArrayListExtra("eventInfo", eventInfo);
            addFriend.putStringArrayListExtra("eventTitles", eventTitles);
            addFriend.putStringArrayListExtra("eventLocation", eventLocation);
            addFriend.putStringArrayListExtra("friendsList", friendsList);
            addFriend.putExtra("userLongitude", userLongitude);
            addFriend.putExtra("userLatitude", userLatitude);
            //addFriend.putExtra("userImageUri", userImageUri);

            android.app.Fragment fragment = new android.app.Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("currentUser", currentUser);
            bundle.putStringArrayList("eventInfo", eventInfo);
            bundle.putStringArrayList("eventTitles", eventTitles);
            bundle.putStringArrayList("eventLocation", eventLocation);
            bundle.putDouble("userLongitude", userLongitude);
            bundle.putDouble("userLatitude", userLatitude);
            //bundle.putString("userImageUri", userImageUri);
            fragment.setArguments(bundle);
            Button b = (Button) view;
            this.startActivity(addFriend);
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
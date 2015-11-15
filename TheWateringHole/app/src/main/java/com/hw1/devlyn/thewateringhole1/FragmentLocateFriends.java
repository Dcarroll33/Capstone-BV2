package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FragmentLocateFriends extends Fragment implements  View.OnClickListener  {
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
    /*private String friendUserName;
    private String friendLongitude;
    private String friendLatitude;*/
    private double currentLongitude;
    private double currentLatitude;

    ConnectDb conDb = new ConnectDb();
    ConnectDb conDb2 = new ConnectDb();


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
        idUserProfile = args.getString("idUserProfile", idUserProfile);
        userName = args.getString("userName", userName);
        description = args.getString("description", description);
        events = args.getString("events", events);
        likes_dislikes = args.getString("likes_dislikes", likes_dislikes);
        userLongitude = args.getDouble("userLongitude", userLongitude);
        userLatitude = args.getDouble("userLatitude", userLatitude);
        friendsList = args.getStringArrayList("friendsList");
        //friendUserName = args.getString("friendUserName", friendUserName);
        //friendLongitude = args.getString("friendLongitude", friendLongitude);
        //friendLatitude = args.getString("friendLatitude", friendLatitude);

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
        return rootView;
        }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    private void setUpMapIfNeeded(View rootView) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            googleMap = ((MapView) rootView.findViewById(R.id.mapview)).getMap();
            //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //googleMap.setMyLocationEnabled(true);
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 18.0f));
                        googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                                .position(new LatLng(userLatitude, userLongitude)).title("You are here!"));
                        for(int i = 0 ; i < friendsList.size(); i = i + 3) {
                            googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                    .position(new LatLng(Double.valueOf(friendsList.get(i+2)), Double.valueOf(friendsList.get(i+1)))).title(friendsList.get(i)));
                        }

                        currentLongitude = arg0.getLongitude();
                        currentLatitude = arg0.getLatitude();

                        try {
                            String[] currentLocation = {"currentLocation", currentUser, String.valueOf(currentLongitude), String.valueOf(currentLatitude)};

                            conDb.execute(currentLocation).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        ArrayList<Double> currentLocation = conDb2.setCurrentCoords();
                        if (currentLocation != null) {
                            Toast.makeText(getActivity(), "User Location Updated", Toast.LENGTH_SHORT).show();
                        } else if (currentLocation == null) {
                            Toast.makeText(getActivity(), "Location Update Failed", Toast.LENGTH_SHORT).show();

                        }

                        googleMap.setOnMyLocationChangeListener(null);
                    }
                });

            }
        }
    }

//}

//public void executeMap(){
        //googleMap.setMyLocationEnabled(true);
        //googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

        //@Override
       // public void onMyLocationChange (Location arg0){


            //googleMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
        //}
    //});



        //LocationManager locationMan = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Criteria bestFit = new Criteria();

        //Get name of best provider
        //String provider = locationMan.getBestProvider(bestFit, true);
        //android.location.LocationListener locationListener;
        //locationMan.requestLocationUpdates(provider, 1000, 0, getActivity());
        //Location myLocation =  locationMan.getLastKnownLocation(provider);//locationMan.getLastKnownLocation(provider);

        //double latitude = myLocation.getLatitude();
        //double longitude = myLocation.getLongitude();
        //LatLng position = new LatLng(latitude, longitude);

        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(position));
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));

   // }


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

            Button b = (Button) view;
            this.startActivity(friendsProfile);
        }
        if (view == AddFriend) {
            Intent addFriend = new Intent(getActivity(), FriendsActivity.class);
            addFriend.putExtra("userId", currentUser);
            addFriend.putExtra("idUserProfile", idUserProfile);
            addFriend.putExtra("userName", userName);
            addFriend.putExtra("description", "" + description);
            addFriend.putExtra("events", events);
            addFriend.putExtra("likes_dislikes", likes_dislikes);
            addFriend.putExtra("userLongitude", userLongitude);
            addFriend.putExtra("userLatitude", userLatitude);
            //addFriend.putExtra("userImageUri", userImageUri);

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
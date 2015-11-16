package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class FragmentMainActivity extends Fragment implements  View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private boolean mIntentInProgress;
    private String currentUser;
    private String idUserProfile;
    private String userName;
    private String description;
    private String events;
    private String likes_dislikes;
    private double userLongitude;
    private double userLatitude;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> friendsList;
    private ArrayList<String> eventInfo;
    //private String friendUserName;
    //private String friendLongitude;
    //private String friendLatitude;
    /*private String eventNameInfo;
    private String numParticipatingInfo;
    private String eventDescriptionInfo;
    private String userImageUri;*/
    /*Fields for the buttons to be used in this class.*/
    Button Events;
    Button Friends;
    Button Profile;
    Button Settings;
    Button SignOut;

    /**
     * Method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param /*param1 Parameter 1.
     * @param /*param2 Parameter 2.
     * @return A new instance of fragment FragmentMainActivity.
     */

    public static FragmentMainActivity newInstance(String param1, String param2) {
        FragmentMainActivity fragment = new FragmentMainActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMainActivity() {
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
        userLongitude = args.getDouble("userLongitude");
        userLatitude = args.getDouble("userLatitude");
        userProfileInfo = args.getStringArrayList("userProfileInfo");
        friendsList = args.getStringArrayList("friendsList");
        eventInfo = args.getStringArrayList("eventInfo");
        //userImageUri = args.getString("userImageUri");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

        getButtons(rootView);

        SignOut = (Button) rootView.findViewById(R.id.sign_out_button);
        Events = (Button) rootView.findViewById(R.id.events_btn);
        Friends = (Button) rootView.findViewById(R.id.friends_btn);
        Profile = (Button) rootView.findViewById(R.id.profile_btn);
        Settings = (Button) rootView.findViewById(R.id.settings_btn);

        return rootView;
    }

    public void getButtons(View v){
        if(v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i < vg.getChildCount(); i++) {
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*This method is for the on screen clicks by the user depending on which button is pushed
        in this case the Events, Friends, Profile or Settings buttons. Once one button has been
        clicked depending on their relationship the screen will switch to the appropriate screen.*/
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_out_button) {
            /*mGoogleApiClient.disconnect();*/
            /*Toast.makeText(super.getActivity().getBaseContext(), "Connected: " /* mGoogleApiClient.isConnected()+"", Toast.LENGTH_LONG).show();*/
            Intent main = new Intent(getActivity(), Login_Screen.class);
            this.startActivity(main);
        }
        if (view == Events) {
            Intent eventsInfo = new Intent(getActivity(), EventsActivity.class);

            eventsInfo.putExtra("userId", currentUser);
            eventsInfo.putExtra("idUserProfile", idUserProfile);
            eventsInfo.putExtra("userName", userName);
            eventsInfo.putExtra("description", "" + description);
            eventsInfo.putExtra("events", events);
            eventsInfo.putExtra("likes_dislikes", likes_dislikes);
            eventsInfo.putExtra("userLongitude", userLongitude);
            eventsInfo.putExtra("userLatitude", userLatitude);
            eventsInfo.putExtra("eventInfo", eventInfo);

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
            fragment.setArguments(bundle);

            Button b = (Button) view;
            this.startActivity(eventsInfo);

        } else if (view == Friends) {
            Intent friends = new Intent(getActivity(), LocateFriendsActivity.class);

            friends.putExtra("userId", currentUser);
            friends.putExtra("idUserProfile", idUserProfile);
            friends.putExtra("userName", userName);
            friends.putExtra("description", "" + description);
            friends.putExtra("events", events);
            friends.putExtra("likes_dislikes", likes_dislikes);
            friends.putExtra("userLongitude", userLongitude);
            friends.putExtra("userLatitude", userLatitude);
            friends.putExtra("friendsList", friendsList);
            //friends.putExtra("userImageUri", userImageUri);

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
            bundle.putStringArrayList("friendsList", friendsList);
            //bundle.putString("userImageUri", userImageUri);
            fragment.setArguments(bundle);


            Button b = (Button) view;
            this.startActivity(friends);
        } else if (view == Profile) {
            Intent profile = new Intent(getActivity(), EditProfileActivity.class);
            profile.putExtra("userId", currentUser);
            profile.putStringArrayListExtra("userProfileInfo", userProfileInfo);
            profile.putExtra("userLongitude", userLongitude);
            profile.putExtra("userLatitude", userLatitude);
            //profile.putExtra("userImageUri", userImageUri);

            Fragment fragment = new Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("currentUser", currentUser);
            bundle.putStringArrayList("userProfileInfo", userProfileInfo);
            bundle.putDouble("userLongitude", userLongitude);
            bundle.putDouble("userLatitude", userLatitude);
            //bundle.putString("userImageUri", userImageUri);
            fragment.setArguments(bundle);


            Button b = (Button) view;
            this.startActivity(profile);
        } else if (view == Settings) {
            Intent settings = new Intent(getActivity(), SettingsActivity.class);

            settings.putExtra("userId", currentUser);
            settings.putExtra("idUserProfile", idUserProfile);
            settings.putExtra("userName", userName);
            settings.putExtra("description", "" + description);
            settings.putExtra("events", events);
            settings.putExtra("likes_dislikes", likes_dislikes);
            settings.putExtra("userLongitude", userLongitude);
            settings.putExtra("userLatitude", userLatitude);

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
            fragment.setArguments(bundle);

            Button b = (Button) view;
            this.startActivity(settings);
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

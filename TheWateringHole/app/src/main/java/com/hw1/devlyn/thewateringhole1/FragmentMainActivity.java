package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentMainActivity extends Fragment /*implements  View.OnClickListener*/{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private boolean mIntentInProgress;
    private String currentUser;
    private double userLongitude;
    private double userLatitude;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> friendsList;
    private ArrayList<String> eventInfo;
    private ArrayList<String> eventTitles;
    private ArrayList<String> eventLocation;
    private TextView InstructionView;
    /*private String userImageUri;*/
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
        userLongitude = args.getDouble("userLongitude");
        userLatitude = args.getDouble("userLatitude");
        userProfileInfo = args.getStringArrayList("userProfileInfo");
        friendsList = args.getStringArrayList("friendsList");
        eventInfo = args.getStringArrayList("eventInfo");
        eventTitles = args.getStringArrayList("eventTitles");
        eventLocation = args.getStringArrayList("eventLocation");
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
        InstructionView = (TextView) rootView.findViewById(R.id.InstructionView);
        InstructionView.setMovementMethod(new ScrollingMovementMethod());

        return rootView;
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

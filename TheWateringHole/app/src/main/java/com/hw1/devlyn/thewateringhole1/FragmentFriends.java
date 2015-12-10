package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @Author: Devlyn Carroll
 * FragmentFriends is used to work along side the friendsActivity and the sliding menu.
 */
public class FragmentFriends extends Fragment implements  View.OnClickListener {
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private boolean mIntentInProgress;

    /**
     * Global variables used to store the values retrieved from the bundle to be used within this
     * class.
     */
    private String currentUser;
    private ArrayList<String> eventInfo;
    private ArrayList<String> eventTitles;
    private ArrayList<String> eventLocation;
    private ArrayList<String> userProfileInfo;
    private ArrayList<String> friendsList;
    String friendUserName;
    String friendEmail;

    /*Field for the button to be used in this class.*/
    Button LocateFriends;
    Button Add;

    EditText friendUserNameText;
    EditText friendUserEmail;

    /**
     * Method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEvents.
     */
    public static FragmentFriends newInstance(String param1, String param2) {
        FragmentFriends fragment = new FragmentFriends();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFriends() {
        // Required empty public constructor
    }

    /**
     * onCreate used to assign the retrieved values from the bundle to local variables used in this
     * class.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getActivity().getIntent().getExtras();
        currentUser = args.getString("userId", currentUser);
        eventInfo = args.getStringArrayList("eventInfo");
        eventTitles = args.getStringArrayList("eventTitles");
        eventLocation = args.getStringArrayList("eventLocation");
        userProfileInfo = args.getStringArrayList("userProfileInfo");
        friendsList = args.getStringArrayList("friendsList");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * onCreateView used to initialize the layout, buttons and editTexts used in this class.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        getButtons(rootView);

        LocateFriends = (Button) rootView.findViewById(R.id.locate_friends_btn);
        Add = (Button) rootView.findViewById(R.id.add);

        friendUserNameText = (EditText) rootView.findViewById(R.id.friendUserName);
        friendUserEmail = (EditText) rootView.findViewById(R.id.friendEmail);

        return rootView;
    }
    /**
     * getButtons used to detect all the buttons and button clicks within the activity.
     * @param v
     */
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
        ConnectDb conDb = new ConnectDb();
        if (view == LocateFriends) {
            Intent events = new Intent(getActivity(), LocateFriendsActivity.class);
            events.putExtra("currentUser", currentUser);
            events.putStringArrayListExtra("eventInfo", eventInfo);
            events.putStringArrayListExtra("eventTitles", eventTitles);
            events.putStringArrayListExtra("eventLocation", eventLocation);
            events.putStringArrayListExtra("userProfileInfo", userProfileInfo);
            events.putStringArrayListExtra("friendsList", friendsList);
            Button b = (Button) view;
            this.startActivity(events);
        }
        /**
         * Add allows for a user to add a friend and have it update in the database as well as check
         * if that friend already exists or not.
         */
        if (view == Add) {
            friendUserName = friendUserNameText.getText().toString();
            friendEmail = friendUserEmail.getText().toString();
            String[] params = {"add", currentUser, friendEmail, friendUserName};
            try {
                conDb.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            int friendInfo = conDb.getFriendSave();
            if(friendInfo == -2) {
                Toast.makeText(getActivity(), "Friend already added!", Toast.LENGTH_SHORT).show();
            } else if (friendInfo > -1) {
                Toast.makeText(getActivity(), "Added Friend!", Toast.LENGTH_SHORT).show();
            } else if (friendInfo == -3) {
                Toast.makeText(getActivity(), "Friend does not exists!", Toast.LENGTH_SHORT).show();

            }
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

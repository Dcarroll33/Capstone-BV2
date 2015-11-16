package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class FragmentEvents extends Fragment implements  View.OnClickListener {
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
    private ArrayList<String> eventInfo;
    private String eventName;
    private String numPart;
    private String eventDesc;
    private double userLongitude;
    private double userLatitude;

    /*Fields for the buttons to be used in this class.*/
    Button saveEvent;
    EditText eventDescription;
    EditText eventTitle;
    EditText numParticipants;
    String title;
    String numPeople;

    /**
     * Method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEvents.
     */
    public static FragmentEvents newInstance(String param1, String param2) {
        FragmentEvents fragment = new FragmentEvents();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEvents() {
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
        eventInfo = args.getStringArrayList("eventInfo");
        /*eventNameInfo= args.getString("eventName", eventNameInfo);
        numParticipatingInfo = args.getString("numParticipating", numParticipatingInfo);
        eventDescriptionInfo = args.getString("eventDescription", eventDescriptionInfo);*/
        userLongitude = args.getDouble("userLongitude", userLongitude);
        userLatitude = args.getDouble("userLatitude", userLatitude);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        getButtons(rootView);

        saveEvent = (Button) rootView.findViewById(R.id.save_btn);
        /*eventDescription = (EditText) rootView.findViewById(R.id.eventDescription);
        eventTitle = (EditText) rootView.findViewById(R.id.eventName);
        numParticipants = (EditText) rootView.findViewById(R.id.numParticipants);*/

        /*eventName = eventInfo.get(2);
        numPart   = eventInfo.get(3);
        eventDesc = eventInfo.get(4);

        eventTitle.setText(eventName);
        eventDescription.setText(eventDesc);
        numParticipants.setText(numPart);*/

        return rootView;
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
        if (view == saveEvent) {
            /*Intent events = new Intent(getActivity(), LocateEventsActivity.class);

            Button b = (Button) view;
            this.startActivity(events);*/
            title = eventTitle.getText().toString();
            numPeople = numParticipants.getText().toString();
            description = eventDescription.getText().toString();

            /*if (description != null && likes_dislikes != null){*/

            ConnectDb conDb = new ConnectDb();
            try {
                String[] params = {"eventSave", currentUser, title, numPeople, description};
                conDb.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            int eventSave = conDb.getEventSave();
            if (eventSave > -1) {
                Toast.makeText(getActivity(), "Event Saved!", Toast.LENGTH_SHORT).show();
            } else if (eventSave == -1) {
                Toast.makeText(getActivity(), "Save Unsuccessful", Toast.LENGTH_SHORT).show();

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


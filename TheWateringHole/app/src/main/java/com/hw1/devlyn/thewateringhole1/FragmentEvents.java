package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * @Author: Devlyn Carroll
 * FragmentEvents works along side the eventsActivity to display the eventInfo and allow button
 * clicks to be detected.
 */
public class FragmentEvents extends Fragment implements  View.OnClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private boolean mIntentInProgress;

    /**
     * Global fields to store the values from the bundle into local variables that are used within
     * this class.
     */
    private String currentUser;
    private String idUserProfile;
    private String userName;
    private String description;
    private String events;
    private String likes_dislikes;
    private ArrayList<String> eventInfo;
    private ArrayList<String> eventLocation;
    private ArrayList<String> eventTitles;

    private ListView eventsList;

    /*Fields for the buttons, editTexts and strings to be used in this class.*/
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

    /**
     * onCreate used to assign the values retrieved from the bundle to local variables used within
     * this class.
     * @param savedInstanceState
     */
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
        eventLocation = args.getStringArrayList("eventLocation");
        eventTitles = args.getStringArrayList("eventTitles");

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * onCreateView used to initialize the layout, buttons and editTexts that are used within this
     * class.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        getButtons(rootView);

        saveEvent = (Button) rootView.findViewById(R.id.save_btn);
        eventDescription = (EditText) rootView.findViewById(R.id.eventDescription);
        eventTitle = (EditText) rootView.findViewById(R.id.eventName);
        numParticipants = (EditText) rootView.findViewById(R.id.numParticipants);
        /**
         * Initializing eventsList to the resource ListView eventsList.
         */
        eventsList = (ListView) rootView.findViewById(R.id.eventsList);

        /**
         * Setting the onItemClickListener for eventsList.
         */
        eventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                Intent LocateEvents = new Intent(getActivity(), LocateEventsActivity.class);
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
                                Intent LocateEvents = new Intent(getActivity(), LocateEventsActivity.class);
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
                                Intent LocateEvents = new Intent(getActivity(), LocateEventsActivity.class);
                                LocateEvents.putStringArrayListExtra("eventInfo", eventInfo);
                                LocateEvents.putStringArrayListExtra("eventTitles", eventTitles);
                                LocateEvents.putStringArrayListExtra("eventLocation", eventLocation);
                                startActivity(LocateEvents);
                            }
                        }
                    });
                }

            }
        });

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

    /**
     * This method is for the on screen clicks by the user depending on which button is pushed
     * in this case the saveEvent button. Once one button has been
     * clicked depending on their relationship the screen will switch to the appropriate screen. As
     * well as a connection is made to the database and the information retrieved from the editTexts
     * is passed to the database.
     */
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

            /*ConnectDb conDb2 = new ConnectDb();
            ArrayList<String> eventTitlesUpdate = conDb2.userEventTitles();

            for(int i = 0 ; i < eventTitlesUpdate.size(); i = i + 1) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.activity_events_listview_text_color,R.id.list_content,
                        eventTitlesUpdate);
                eventsList.setAdapter(arrayAdapter);

            }*/
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


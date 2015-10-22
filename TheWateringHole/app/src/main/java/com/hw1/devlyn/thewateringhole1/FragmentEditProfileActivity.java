package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
/*import android.app.Fragment;*/
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by drcar on 9/20/2015.
 */
public class FragmentEditProfileActivity extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = 0;
    private static final int SELECT_IMAGE = 0;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /*Fields for the buttons to be used in this class.*/

    EditText Description;
    EditText Likes_Dislikes;
    EditText UserName;

    String currentUser;
    String idUserProfile;
    String userName;
    String description;
    String events;
    String likes_dislikes;

    Button Save;
    Button UploadImage;
    Button Load;

    ConnectDb conDb = new ConnectDb();
    ConnectDb conDb2 = new ConnectDb();

    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;

    private static AsyncTask<String, Void, String> dbCon;
    private String name;

    /**
     * Method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param /*param1 Parameter 1.
     * @param /*param2 Parameter 2.
     * @return A new instance of fragment FragmentEvents.
     */
    public static FragmentEditProfileActivity newInstance(String param1, String param2) {
        FragmentEditProfileActivity fragment = new FragmentEditProfileActivity();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentEditProfileActivity() {
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

        MyApplicationClass.MySQLAccess dao = ConnectDb.getDao();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /*try {
            /*dao.readDataBase();*/
            /*Log.d("HERE", "We made it.. UserID is: " + currentUser);
            ArrayList<String> getProfileResult = dao.getUserProfileInfo(String.valueOf(currentUser), description, likes_dislikes);
            Log.d("ArrayList<String>", "The value of getProfileResult is : " + getProfileResult);
            Description.setText(getProfileResult.get(2));
            Log.d("DESCRIPTION", "SET DESCRIPTION" + Description);
            Likes_Dislikes.setText(getProfileResult.get(3));
            Log.d("LIKES_DISLIKES", "SET LIKES_DISLIKES" + Likes_Dislikes);
            /*UserName.setText(getProfileResult.get(2));
            Log.d("USERNAME", "SET USERNAME" + UserName);*/

        /*} catch (Exception e){
            Log.e("Sync Failed", e.toString());
        }*/



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_profile_activity, container, false);

        getButtons(rootView);

        Description = (EditText) rootView.findViewById(R.id.Description);
        Likes_Dislikes = (EditText) rootView.findViewById(R.id.Likes_Dislikes);
        UserName = (EditText) rootView.findViewById(R.id.Username);
        Save = (Button) rootView.findViewById(R.id.save_btn);
        UploadImage = (Button) rootView.findViewById(R.id.uploadImage);
        Load = (Button) rootView.findViewById(R.id.load_btn);

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
        if (view == Save) {
            description = Description.getText().toString();
            likes_dislikes = Likes_Dislikes.getText().toString();
            name = UserName.getText().toString();

            /*if (description != null && likes_dislikes != null){*/


            try {
                String[] params = {"save", /*String.valueOf(conDb2.getUserProfileInfo())*/ String.valueOf(conDb.getUserId()), name, description, likes_dislikes};
                conDb.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            int save = conDb.getSave();
            if (save > -1) {
                Log.d("EDIT PROFILE", "Sent: " + description + ", " + likes_dislikes + "," + name);
                Toast.makeText(getActivity(), "Updated Profile!", Toast.LENGTH_SHORT).show();
            } else if (save == -1) {
                Log.d("Description", "Sent: " + description + ", " + "Likes/Dislikes " + likes_dislikes + "," + name);
                Toast.makeText(getActivity(), "Save Unsuccessful", Toast.LENGTH_SHORT).show();

            }
        }
        if (view == UploadImage) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
        }
        if(view == Load) {
            UserName.setText(userName);
            Description.setText(description);
            Likes_Dislikes.setText(likes_dislikes);

            try {
                String[] params = {"load",String.valueOf(conDb2.getUserProfileInfo()), String.valueOf(conDb2.getUserId()), description, likes_dislikes, name};
                conDb2.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
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

package com.hw1.devlyn.thewateringhole1;

import android.app.Activity;
/*import android.app.Fragment;*/
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by drcar on 9/20/2015.
 * FragmentEditProfileActivity is the fragment that is called from EditProfileActivity. This
 * fragment allows for the buttons and editTexts that the user interacts with to be accessible
 * alongside the navigation drawer. The fragment implements View.OnClickListener to detect button
 * clicks.
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

    /*Fields for the editTexts to be used in this class.*/

    EditText Description;
    EditText Likes_Dislikes;
    EditText UserName;

    private ImageView profileImage;

    /*Fields for the items that are being passed in through the bundle.*/
    private String currentUser;
    private String idUserProfile;
    private String userName;
    private String description;
    private String events;
    private String likes_dislikes;
    private Uri fileName;
    private String userImage;
    private String userImageUri;
    private Uri userImageUriR;
    private double userLongitude;
    private double userLatitude;

    /*Fields for the buttons that are in the fragment.*/
    Button Save;
    Button UploadImage;
    //Button Load;

    /*Initializing a new ConnectDb() object to a ConnectDb type variable.*/
    ConnectDb conDb = new ConnectDb();

    //private static final int REQUEST_CODE = 1;
    //private Bitmap bitmap;

    private static AsyncTask<String, Void, String> dbCon;
    private int PICK_IMAGE_REQUEST = 1;

    /**
     * Method to create a new instance of
     * this fragment using the provided parameters.
     *
     * java.lang.String@param /*param1 Parameter 1.
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

    /*onCreate method that retrieves the values of the items from the bundle that is passed in.*/
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
        userImageUri = args.getString("userImageUri", userImageUri);

        MyApplicationClass.MySQLAccess dao = ConnectDb.getDao();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /* onCreateView method that allows the buttons and editTexts for this fragment to be detected
       by using rootView. The rootView is referenced once the fragment is inflated.*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*This is where the fragment is inflated.*/
        View rootView = inflater
                .inflate(R.layout.fragment_edit_profile_activity, container, false);

        MyApplicationClass.MySQLAccess dao = ConnectDb.getDao();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getButtons(rootView);

        Description = (EditText) rootView.findViewById(R.id.Description);
        Likes_Dislikes = (EditText) rootView.findViewById(R.id.Likes_Dislikes);
        UserName = (EditText) rootView.findViewById(R.id.Username);
        Save = (Button) rootView.findViewById(R.id.save_btn);
        UploadImage = (Button) rootView.findViewById(R.id.uploadImage);
        //Load = (Button) rootView.findViewById(R.id.load_btn);
        profileImage = (ImageView) rootView.findViewById(R.id.profileImage);

        /*This is where the editText fields are set with the Strings or Uri from whatever is passed
           in from the bundle.*/
        UserName.setText(userName);
        Description.setText(description);
        Likes_Dislikes.setText(likes_dislikes);

        /*The userImageUri is parsed through here because the entire image file path needs to be
          exact. This is so the image can be found and set in the imageView.
         */
        userImageUriR = Uri.parse(userImageUri);
        profileImage.setImageURI(userImageUriR);

        return rootView;
    }

    /*This getButtons method is used to find all the buttons that are within the View of the fragment.
      Used to make button detection more efficient. */
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

    /*@Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /*This method is for the on screen clicks by the user depending on which button is pushed
        in this case the Events, Friends, Profile or Settings buttons. Once one button has been
        clicked depending on their relationship the screen will switch to the appropriate screen.*/
    @Override
    public void onClick(View view) {
        if (view == Save) {
            description = Description.getText().toString();
            likes_dislikes = Likes_Dislikes.getText().toString();
            userName = UserName.getText().toString();
            //userImage = fileName;
            /*Try catch block used to create a String array that contains the fields that are used
              in the activity. The String array params then gets passed into my conDb to be updated
              in the database.
             */
            try {
                String[] params = {"save", currentUser, userName, description, likes_dislikes, userImage};
                conDb.execute(params).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            int save = conDb.getSave();
            if (save > -1) {
                Toast.makeText(getActivity(), "Updated Profile!", Toast.LENGTH_SHORT).show();
            } else if (save == -1) {
                Toast.makeText(getActivity(), "Save Unsuccessful", Toast.LENGTH_SHORT).show();

            }
        }

        /*This if statement is for if the upload image button is clicked. When the button is clicked
          and intent is fired to pull up the device's image gallery, from which the user can choose
          and image.
         */
        if (view == UploadImage) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    /*onActivityResult is used to grab the data from the image that the user has chosen. Once the
      data has been retrieved, a bitmap is used to store that data. The uri from that data is then
      pulled to get the scheme of the filepath.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && null != data && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                String scheme = uri.getScheme();

                /*This if, else if statement is used to check to see if the uri contains "file" or
                  "content" and if it does send the cursor through to get the columnIndex.
                 */
                if (scheme.equals("file")) {
                    fileName = uri;
                }
                else if (scheme.equals("content")) {
                    String[] proj = { MediaStore.Images.Media.TITLE };
                    Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
                    if (cursor != null && cursor.getCount() != 0) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                        cursor.moveToFirst();
                        fileName = uri;//cursor.getString(columnIndex);
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                userImage = fileName.toString();
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
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

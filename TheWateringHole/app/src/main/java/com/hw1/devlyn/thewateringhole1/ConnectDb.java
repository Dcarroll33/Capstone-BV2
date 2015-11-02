package com.hw1.devlyn.thewateringhole1;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.ArrayList;

/**
 * Created by Devlyn on 4/21/2015.
 */
public class ConnectDb extends AsyncTask<String, Void, Integer> {

    private Exception exception;

    private int result = -1;

    private int userId = -1;

    private int save = -1;

    private int eventSave = -1;

    private String description = null;

    public ArrayList<String> load = null;

    public ArrayList<Double> coords = null;

    private ArrayList<String> userEventInfo = null;


    /*private ArrayList<String> getUserProfileResult =  null;
    private String likes_dislikes = null;*/

    private HttpClient httpclient;

    private HttpPost httppost;

    private ArrayList<NameValuePair> nameValuePairs;

    private HttpResponse response;

    private Bitmap bitmap;


    public static MyApplicationClass.MySQLAccess dao = new MyApplicationClass.MySQLAccess();

    public static MyApplicationClass.MySQLAccess getDao() {
        return dao;
    }

    protected Integer doInBackground(String... strings) {
        try {
            if (strings[0] == "register") {
                MyApplicationClass.MySQLAccess.readDataBase();
                Log.d("connectDb", "Registration success");
                result = dao.registerUser(strings[1], strings[2], strings[3]);
                return result;

            } else if (strings[0] == "login") {
                MyApplicationClass.MySQLAccess.readDataBase();
                int result = dao.loginUser(strings[1], strings[2]);

                if (result != -1) {
                    Log.d("ConnectDb", "Login successful! User ID is: " + result);
                    userId = result;
                    //return 0;
                    //MyApplicationClass.MySQLAccess.readDataBase();
                    ArrayList<String> profileInfoResult = dao.getUserProfileInfo(userId);
                    if (profileInfoResult != null){
                        Log.d("ConnectDb", "User info passed! Desc:" + profileInfoResult.get(3) + "Like:" + profileInfoResult.get(5));
                        load = profileInfoResult;
                    }
                    ArrayList<Double> userCoordsResult = dao.getUserCoords(userId);
                    if (userCoordsResult != null){
                        Log.d("ConnectDb", "userLongitude" + userCoordsResult.get(1) + "userLatitude" + userCoordsResult.get(2));

                        coords = userCoordsResult;
                    }
                    ArrayList<String> userEventInfoResult = dao.userEventInfo(userId);
                    if (userEventInfoResult != null){
                        userEventInfo = userEventInfoResult;
                    }
                    return 0;
                } else {
                    Log.d("ConnectDb", "Login failed :'(");
                    return -1;
                }

            } else if (strings[0] == "save") {
                MyApplicationClass.MySQLAccess.readDataBase();
                int saveResult = dao.userProfile(strings[1], strings[2], strings[3], strings[4]);
                if (saveResult != -1) {
                    Log.d("ConnectDb", "Save successful");
                    save = saveResult;
                    return 0;
                } else {
                    return -1;
                }
            } else if (strings[0] == "eventSave") {
                MyApplicationClass.MySQLAccess.readDataBase();
                int eventResult = dao.eventInfo(strings[1], strings[2], strings[3], strings[4]);
                if (eventResult != -1) {
                    Log.d("ConnectDb", "Event saved");
                    eventSave = eventResult;
                    return 0;
                } else {
                    return -1;
                }
            } else if (strings[0] == "load") {
                MyApplicationClass.MySQLAccess.readDataBase();
                ArrayList<String> getUserProfileResult = dao.getUserProfileInfo(userId);
                Log.d("ConnectDb", "getUserProfileResult contains : " + getUserProfileResult.get(1) + getUserProfileResult.get(2));
               if (getUserProfileResult != null) {
                    getUserProfileResult.get(1);
                    getUserProfileResult.get(2);
                    getUserProfileResult.get(3);
                    load = getUserProfileResult;
                    return 0;
                } else {
                    return -1;
                }
            }
        } catch (Exception e) {
            Log.e("upload failed", e.toString());
        }
        return null;
    }

    public int getResult(){
        return result;
    }

    public int getUserId(){
        return userId;
    }

    public ArrayList<Double> getUserCoords() {
        return coords;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getUserProfileInfo(){
        return load;
    }

    public ArrayList<String> userEventInfo() {
       return userEventInfo;
    }

    public int getSave(){
        return save;
    }

    public int getEventSave(){
        return eventSave;
    }

}

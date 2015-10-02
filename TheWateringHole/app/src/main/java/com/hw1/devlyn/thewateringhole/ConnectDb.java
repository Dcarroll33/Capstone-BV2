package com.hw1.devlyn.thewateringhole;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Devlyn on 4/21/2015.
 */
public class ConnectDb extends AsyncTask<String, Void, Integer> {

    private Exception exception;

    private int result = -1;

    private int userId = -1;

    private int save = -1;

    private int eventSave = -1;

    private String idUserProfile = null;

    private String description = null;

    private String likes_dislikes = null;

    public ArrayList<String> load = null;

    private ArrayList<String> getProfileInfo = null;


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
                dao.readDataBase();
                Log.d("connectDb", "Registration success");
                result = dao.registerUser(strings[1], strings[2], strings[3]);
                return result;

            } else if (strings[0] == "login") {
                dao.readDataBase();
                int result = dao.loginUser(strings[1], strings[2]);
                /*ArrayList<String> getUserProfileInfo = dao.getUserProfileInfo(strings[1], strings[2], strings[3]);*/
                if (result != -1) {
                    Log.d("ConnectDb", "Login successful! User ID is: " + result);
                    userId = result;
                    /*getUserProfileResult = getUserProfileInfo;*/
                    return 0;
                } else {
                    Log.d("ConnectDb", "Login failed :'(");
                    return -1;
                }
            } else if (strings[0] == "save") {
                dao.readDataBase();
                int saveResult = dao.userProfile(strings[1], strings[2], strings[3], strings[4]);
                if (saveResult != -1) {
                    Log.d("ConnectDb", "Save successful");
                    save = saveResult;
                    return 0;
                } else {
                    return -1;
                }
            } else if (strings[0] == "eventSave") {
                dao.readDataBase();
                int eventResult = dao.userEvents(strings[1], strings[2], strings[3]);
                if (eventResult != -1) {
                    Log.d("ConnectDb", "Event saved");
                    eventSave = eventResult;
                    return 0;
                } else {
                    return -1;
                }
            } else if (strings[0] == "load") {
                dao.readDataBase();
                ArrayList<String> getUserProfileResult = dao.getUserProfileInfo(strings[1], strings[2], strings[3], strings[4]);
                Log.d("ConnectDb", "getUserProfileResult contains : " + getUserProfileResult.get(1) + getUserProfileResult.get(2)
                                                        + getUserProfileResult.get(3) + getUserProfileResult.get(4));
                if (getUserProfileResult != null) {
                    getUserProfileResult.get(1);
                    getUserProfileResult.get(2);
                    getUserProfileResult.get(3);
                    getUserProfileResult.get(4);
                    /*load = getUserProfileResult;*/
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

    public ArrayList<String> getUserProfileInfo(){
        return load;
    }

    /*public int getUserProfileId(){
        return idUserProfile;
    }

    /*public String getLikesDislikes(){
        return likes_dislikes;
    }*/

    public int getSave(){
        return save;
    }

    public int getEventSave(){
        return eventSave;
    }
}

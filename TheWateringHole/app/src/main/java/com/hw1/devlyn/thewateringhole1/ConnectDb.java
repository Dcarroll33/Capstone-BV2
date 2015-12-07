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
 * @Author: Devlyn Carroll
 * @Date: 4/21/2015
 * ConnectDb is the class used to run as an AsyncTask alongside the application.
 */
public class ConnectDb extends AsyncTask<String, Void, Integer> {

    private Exception exception;

    /**
     * result is the global variable that is used to compare what is returned when the connection is
     * made and if anything is in the database.
     */
    private int result = -1;

    /**
     *
     */
    private int idfriends = -1;

    /**
     * userId is the global variable that is used when the the user registers or logs into the
     * application.
     */
    private int userId = -1;

    /**
     * save is the global variable that is used when the user attempts to save.
     */
    private int save = -1;

    /**
     * eventSave is the global variable that is used when the user attempts to save and event.
     */
    private int eventSave = -1;

    /**
     * friendSave is the global variable that is used when the user adds a friend.
     */
    private int friendSave = -1;

    private String description = null;

    public ArrayList<String> load = null;

    /**
     * coords is the global ArrayList of doubles that stores the user's current latitude & longitude.
     */
    public ArrayList<Double> coords = null;

    /**
     * friendCoords is the global ArrayList of strings that stores the user's friends longitudes &
     * latitudes.
     */
    public ArrayList<String> friendCoords = null;

    /**
     * userEventInfo is the global ArrayList of strings that stores the strings eventName, number
     * of people participating and a description of the event.
     */
    private ArrayList<String> userEventInfo = null;

    /**
     * userEventTitles is the global ArrayList of strings that stores the string eventName.
     */
    private ArrayList<String> userEventTitles = null;

    /**
     * friendCurrentLocation is the global ArrayList of doubles that stores the user's friend's
     * latitude & longitude.
     */
    private ArrayList<String> friendCurrentLocation = null;

    /**
     * currentUserLocation is the global ArrayList of doubles that stores the user's current
     * latitude & longitude.
     */
    private ArrayList<Double> currentUserLocation = null;

    /**
     * userEventLocation is the global ArrayList of strings that stores the latitude & longitude of
     * the events.
     */
    public ArrayList<String> userEventLocation = null;

    /**
     * dao is the initialization of the of the MyApplicationClass.MySQLAccess.
     */
    public static MyApplicationClass.MySQLAccess dao = new MyApplicationClass.MySQLAccess();

    public ConnectDb() {
    }


    /**
     * getter method to return the new SQL Access dao.
     * @return
     */
    public static MyApplicationClass.MySQLAccess getDao() {
        return dao;
    }

    public ConnectDb(Integer userId){
        this.userId = userId;
    }

    /**
     * doInBackground is an Integer method that passes Strings in as parameters and checks to see
     * which string has been called.
     * @param strings
     * @return
     */
    protected Integer doInBackground(String... strings) {
        try {
            /**
             * Checks to see if the string is register and then reads the database. It then connects
             * to the registerUser method that is in MyApplicationClass. Then it assigns result to a
             * value other than -1 if the method successfully sent and retrieved those values.
             */
            if (strings[0] == "register") {
                MyApplicationClass.MySQLAccess.readDataBase();
                Log.d("connectDb", "Registration success");
                result = dao.registerUser(strings[1], strings[2], strings[3]);
                return result;

                /**
                 * Checks to see if the string is login and then reads the database. It then
                 * connects to the loginUser method in MyApplicationClass. Then it assigns result to
                 * to a value other than -1 if the method successfully sent and retrieved those
                 * values.
                 */
            } else if (strings[0] == "login") {
                MyApplicationClass.MySQLAccess.readDataBase();
                int result = dao.loginUser(strings[1], strings[2]);

                /**
                 * Checks to see if the result is not -1 and if it's not assign that value to the
                 * userId.
                 */
                if (result != -1) {
                    Log.d("ConnectDb", "Login successful! User ID is: " + result);
                    userId = result;
                    //return 0;
                    //MyApplicationClass.MySQLAccess.readDataBase();

                    /**
                     * profileInfoResult is assigned the values from getUserProfileInfo method in
                     * MyApplicationClass based on the userId passed in.
                     * Checks to see if profileInfoResult is not null, then assigns the
                     * profileInfoResult to the ArrayList of strings load;
                     */
                    ArrayList<String> profileInfoResult = dao.getUserProfileInfo(userId);
                    if (profileInfoResult != null) {
                        Log.d("ConnectDb", "User info passed! Desc:" + profileInfoResult.get(3) + "Like:" + profileInfoResult.get(5));
                        load = profileInfoResult;
                    }

                    /**
                     * userCoordsResult is assigned the values from getUserCoords method in
                     * MyApplicationClass based on the userId passed in. Then it checks to see if
                     * userCoordsResult is not null, then assigns the userCoordsResult to the
                     * ArrayList of strings coords.
                     */
                    ArrayList<Double> userCoordsResult = dao.getUserCoords(userId);
                    if (userCoordsResult != null) {
                        Log.d("ConnectDb", "userLongitude" + userCoordsResult.get(0) + "userLatitude" + userCoordsResult.get(1));
                        coords = userCoordsResult;
                    }

                    /**
                     * friendCoordsResult is assigned the values from getFriendCoords method in
                     * MyApplicationClass based on the userId passed in. Then it checks to see if
                     * friendCoordsResult is not null, then assigns the friendCoordsResult to the
                     * ArrayList of strings friendCoords.
                     */
                    ArrayList<String> friendCoordsResult = dao.getFriendCoords(userId);
                    if (friendCoordsResult != null) {
                        Log.d("ConnectDB", "friendLongitude" + friendCoordsResult.get(0) + " friendLatitude" + friendCoordsResult.get(1));
                        friendCoords = friendCoordsResult;
                    }

                    /**
                     * userEventInfoResult is assigned the values from userEventInfo method in
                     * MyApplicationClass based on the userId passed in. Then it checks to see if
                     * userEventInfoResult is not null, then assigns the userEventInfoResult to the
                     * ArrayList of strings userEventInfo.
                     */
                    ArrayList<String> userEventInfoResult = dao.userEventInfo(userId);
                    if (userEventInfoResult != null) {
                        userEventInfo = userEventInfoResult;
                    }

                    /**
                     * userEventTitleResult is assigned the values from the userEventTitles method
                     * in MyApplicationClass based on the userId passed in. Then it checks to see if
                     * userEventTitleResult is not null, then assigns the userEventTitleResult to
                     * the ArrayList of strings userEventTitles.
                     */
                    ArrayList<String> userEventTitlesResult = dao.userEventTitles(userId);
                    if (userEventTitlesResult != null) {
                        userEventTitles = userEventTitlesResult;
                    }

                    /**
                     * eventLocation is assigned the values from the userEventLocation method in
                     * MyApplicationClass based on the userId passed in. Then it checks to see if
                     * eventLocation is not null, then assigns the eventLocation to the ArrayList
                     * of strings userEventLocation.
                     */
                    ArrayList<String> eventLocation = dao.userEventLocation(userId);
                    if (eventLocation != null) {
                        userEventLocation = eventLocation;
                    }
                    /**
                     * returns success.
                     */
                    return 0;

                    /**
                     * else the login failed return failure.
                     */
                 } else {
                    Log.d("ConnectDb", "Login failed :'(");
                    return -1;
                 }

                /**
                 * Checks to see if the string is save and then reads the database. Then saveResult
                 * is assigned to some value other than -1. Then if saveResult is something other
                 * than -1 saveResult is assigned to the integer save, return success else return
                 * failure.
                 */
            } else if (strings[0] == "save") {
                MyApplicationClass.MySQLAccess.readDataBase();
                int saveResult = dao.userProfile(strings[1], strings[2], strings[3], strings[4]/*, strings[5]*/);
                if (saveResult != -1) {
                    Log.d("ConnectDb", "Save successful");
                    save = saveResult;
                    return 0;
                } else {
                    return -1;
                }

                /**
                 * Checks to see if the string is eventSave and then reads the database. Then
                 * eventResult is assigned to some value other than -1 based off the values that
                 * the eventInfo method in MyApplicationClass returns. Then if eventResult is
                 * something other than -1 eventResult is assigned to the integer eventSave,
                 * return success else return failure.
                 */
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
            }else if (strings[0] == "eventUpdate") {
                ArrayList<String> userEventTitlesResult = dao.userEventTitles(userId);
                if (userEventTitlesResult != null) {
                    userEventTitles = userEventTitlesResult;
                }

                /**
                 * Checks to see if the string is load and then reads the database. Then
                 * getUserProfileResult is assigned the values that the getUserProfileInfo method in
                 * MyApplicationClass returns. Then if getUserProfileResult is not null, it pulls
                 * the eventName(1), number of people participating(2) and description(3) from the
                 * ArrayList of strings getUserProfileResult.
                 */
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

                /**
                 * Checks to see if the string is add and then reads the database. friendResult is
                 * assigned some value other than -1 based off the values that the addFriend method
                 * in MyApplicationClass returns. Then if friendResult is something other than -1
                 * friendResult is assigned to the integer friendSave.
                 */
            } else if (strings[0] == "add") {
                MyApplicationClass.MySQLAccess.readDataBase();
                int friendResult = dao.addFriend(strings[1], strings[2], strings[3]);
                if(friendResult != -1) {
                    Log.d("Friend added", "Friend added");
                    friendSave = friendResult;
                    return friendSave;
                }

                /**
                 * Checks to see if the string is friendLocation and then reads the database.
                 * friendLocation is assigned the values that the setFriendCoords method in
                 * MyApplicationClass returns. Then if friendLocation is not null, add the values
                 * friendName, friendLatitude & friendLongitude to the ArrayList friendLocation.
                 * Then assigns friendLocation to the ArrayList of doubles friendCurrentLocation,
                 * return success else return failure.
                 */
            //}/* else if (strings[0] == "friendLocation") {
               /* MyApplicationClass.MySQLAccess.readDataBase();
                ArrayList<Double> friendLocation = dao.setFriendCoords(strings[1], Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
                if (friendLocation != null){
                    friendLocation.add(0, Double.valueOf(strings[1]));
                    friendLocation.add(1, Double.valueOf(strings[2]));
                    friendLocation.add(2, Double.valueOf(strings[3]));
                    friendCurrentLocation = friendLocation;
                    return 0;
                } else {
                    return -1;
                }*/

                /**
                 * Checks to see if the string is currentLocation and then reads the database.
                 * currentLocation is assigned the values that the setCurrentCoords method in
                 * MyApplicationClass returns. Then if friendLocation is not null, add the values
                 * userName, userLatitude, userLongitude to the ArrayList currentLocation. Then
                 * assigns currentLocation to the ArrayList of doubles currentUserLocation, return
                 * success else return failure.
                 */
        } else if (strings[0] == "currentLocation") {
            MyApplicationClass.MySQLAccess.readDataBase();
            ArrayList<Double> currentLocation = dao.setCurrentCoords(strings[1], Double.parseDouble(strings[2]), Double.parseDouble(strings[3]));
            if (currentLocation != null){
                currentLocation.add(0, Double.valueOf(strings[1]));
                currentLocation.add(1, Double.valueOf(strings[2]));
                currentLocation.add(2, Double.valueOf(strings[3]));
                currentUserLocation = currentLocation;
                return 0;
            } else {
                return -1;
            }
        } else if (strings[0] == "friendUpdatedLoc") {
                MyApplicationClass.MySQLAccess.readDataBase();
                ArrayList<String> friendCoordsResult = dao.getFriendCoords(userId);
                if (friendCoordsResult != null) {
                    Log.d("ConnectDB", "friendLongitude" + friendCoordsResult.get(1) + " friendLatitude" + friendCoordsResult.get(2));
                    friendCurrentLocation = friendCoordsResult;
                }
            }
            /**
             * catch block for the try block to capture exceptions.
             */
        } catch (Exception e) {
            Log.e("upload failed", e.toString());
        }
        return null;
    }

    /**
     * getter to return the int result.
     * @return
     */
    public int getResult(){
        return result;
    }

    /**
     * getter to return the int userId.
     * @return
     */
    public int getUserId(){
        return userId;
    }

    /**
     * getter to return the ArrayList of doubles coords.
     * @return
     */
    public ArrayList<Double> getUserCoords() {
        return coords;
    }

    /**
     * getter to return the ArrayList of strings friendCoords.
     * @return
     */
    public ArrayList<String> getFriendCoords(){

        return friendCoords;
    }

    /**
     * getter to return the ArrayList of doubles friendCurrentLocation.
     * @return
     */
    public ArrayList<String> setFriendCoords() throws Exception {

        return friendCurrentLocation;
    }

    /**
     * getter to return the ArrayList of doubles currentUserLocation.
     * @return
     */
    public ArrayList<Double> setCurrentCoords(){
        return currentUserLocation;
    }

    /**
     * getter to return the ArrayList of strings userEventLocation.
     * @return
     */
    public ArrayList<String> getEventLocation() {
        return userEventLocation;
    }

    /**
     * getter to return the string decription.
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * getter to return the ArrayList of strings load.
     * @return
     */
    public ArrayList<String> getUserProfileInfo(){
        return load;
    }

    /**
     * getter to return the ArrayList of strings userEventInfo.
     * @return
     */
    public ArrayList<String> userEventInfo() {
       return userEventInfo;
    }

    /**
     * getter to return the ArrayList of strings userEventTitles.
     * @return
     */
    public ArrayList<String> userEventTitles(){
        return userEventTitles;
    }

    /**
     * getter to return the int save.
     * @return
     */
    public int getSave(){
        return save;
    }

    /**
     * getter to return the int eventSave.
     * @return
     */
    public int getEventSave(){
        return eventSave;
    }

    /**
     * getter to return the int friendSave.
     * @return
     */
    public int getFriendSave() {
        return friendSave;
    }
}

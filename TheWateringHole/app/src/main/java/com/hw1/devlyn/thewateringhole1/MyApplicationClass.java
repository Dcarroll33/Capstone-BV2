package com.hw1.devlyn.thewateringhole1;

import android.app.Application;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Devlyn on 4/8/2015.
 */
public class MyApplicationClass extends Application {

    public static class MySQLAccess {
        private static Connection connect = null;
        private static Statement statement = null;
        private static PreparedStatement preparedStatement = null;
        private static ResultSet resultSet = null;
        private static final String DBCON = "jdbc:mysql://direct.cwardcode.com/wateringhole";
        private static final String USERNAME = "devlyn";
        private static final String USERPSWD = "wateringcap";

        /*This method is for when the user registers an account. The userName, userPswd and email
          are inserted into the users DB table.
         */
        public int registerUser(String userName, String userPswd, String email) {
            try {
                preparedStatement = connect
                        .prepareStatement("select * from users where userName=?");
                preparedStatement.setString(1, userName);
                ResultSet userNameResult = preparedStatement.executeQuery();
                if (userNameResult.next()) {
                    return -2;
                }
                preparedStatement = connect
                        .prepareStatement("insert into users (userName, password, email) values (?,?,?)");
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, userPswd);
                preparedStatement.setString(3, email);
                Log.d("SQLConnect", "added to DB");
                return preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Log.e("MYMYSQLACCESS", e.getMessage());
            }
            return -1;
        }

        public static void readDataBase() throws Exception {
            try {
                // This will load the MySQL driver, each DB has its own driver
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                // Setup the connection with the DB
                Log.d("Before Connect", "Connecting to DB");
                connect = DriverManager
                        .getConnection(DBCON, USERNAME, USERPSWD);
                Log.d("Connected","Reading DB");
            } catch (Exception e) {
                Log.e("MAC","Error occured! Error was" + e.getMessage());
                e.getStackTrace();
                throw e;
            }

        }

        public int loginUser(String userName, String userPswd){
            try {
                preparedStatement = connect
                        .prepareStatement("select * from users where userName=? and password=?");
                        preparedStatement.setString(1, userName);
                        preparedStatement.setString(2, userPswd);
                        ResultSet userResult =  preparedStatement.executeQuery();
                    if (!userResult.next()) {
                         return -1;
                    }else{
                        Log.d("CheckPoint", "UserResult" + userResult.getInt("idUsers"));
                return userResult.getInt("idUsers");
            }
            } catch (SQLException e){
                e.printStackTrace();
                }
                return -1;
            }

        public int userProfile(String userId, String name, String description, String likes_dislikes){
            try {
                Log.d("UserProfile", "Got data: userId: " + userId + " desc: " + description + " likes-dislikes: " + likes_dislikes + "userName: " + name);
                preparedStatement = connect
                        .prepareStatement("select * from userProfile where userId=?");
                preparedStatement.setString(1, userId);
                Log.d("lol", "About to execute prepstatement1");
                ResultSet userProfileResult =  preparedStatement.executeQuery();
                Log.d("SELECT", "inside select");
                if (!userProfileResult.next()) {

                    preparedStatement = connect
                            .prepareStatement("insert into userProfile (userName, description, likes_dislikes) values (?,?,?)");
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, likes_dislikes);
                    Log.d("SQLConnect", "added to DB");
                    return preparedStatement.executeUpdate();
                } else {
                    preparedStatement = connect
                            .prepareStatement("update userProfile set userName=?, description=?, likes_dislikes=? where idUserProfile=?");

                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, likes_dislikes);
                    preparedStatement.setString(4, userId);
                    Log.d("UPDATE", "userId already exists");
                    return preparedStatement.executeUpdate();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
            return -1;
        }

        public ArrayList<String> getUserProfileInfo(int userId){
            ArrayList<String> profileInfo = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select * from userProfile where userId=?;"/*and userId=? and description=? and likes_dislikes=?"*/);
                preparedStatement.setInt(1, userId);
                //Log.d("MyApplicationClass", "idUserProfile" + profileInfo.set(1, String.valueOf(userId)));
                ResultSet getProfileResult =  preparedStatement.executeQuery();
                getProfileResult.next();

                Log.d("MyApplication", "getProfileResult is :" + getProfileResult);

                int userIdR = getProfileResult.getInt(1);
                int idUserProfileR = getProfileResult.getInt(2);
                String profileNameR = getProfileResult.getString(3);
                String descriptionR = getProfileResult.getString(4);
                String eventsR = getProfileResult.getString(5);
                String likes_dislikes_R = getProfileResult.getString(6);

                profileInfo.add(0, String.valueOf(userIdR));
                profileInfo.add(1, String.valueOf(idUserProfileR));
                profileInfo.add(2, profileNameR);
                profileInfo.add(3, descriptionR);
                profileInfo.add(4, eventsR);
                profileInfo.add(5, likes_dislikes_R);

            } catch (SQLException e){
            e.printStackTrace();
        }
        return profileInfo;
        }

        public int userEvents(String eventName, String eventDescription, String numParticipating){
            try {
                Log.d("userEvent", "eventName: " + eventName + " description: " + eventDescription);
                preparedStatement = connect
                        .prepareStatement("select * from events where eventName =?");
                preparedStatement.setString(1, eventName);
                Log.d("lol", "About to execute prepstatement1");
                ResultSet userProfileResult =  preparedStatement.executeQuery();
                Log.d("SELECT", "inside select");
                if (!userProfileResult.next()) {

                    preparedStatement = connect
                            .prepareStatement("insert into events (eventName, description, numParticipating) values (?,?,?)");
                    preparedStatement.setString(1, eventName);
                    preparedStatement.setString(2, eventDescription);
                    preparedStatement.setString(3, numParticipating);
                    Log.d("SQLConnect", "added to DB");
                    return preparedStatement.executeUpdate();
                } else {
                    preparedStatement = connect
                            .prepareStatement("update events set eventName=?, description=?, numParticipating=? where (select * from users where idusers=?)");
                    preparedStatement.setString(1, eventName);
                    preparedStatement.setString(2, eventDescription);
                    preparedStatement.setString(3, numParticipating);
                    Log.d("UPDATE", "userId already exists");
                    return preparedStatement.executeUpdate();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
            return -1;

        }
        // Closes the resultSet
        private static void close() {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (statement != null) {
                    statement.close();
                }

                if (connect != null) {
                    Log.d("CONN CLOSED", "Connection Closed");
                    connect.close();
                }
            } catch (Exception e) {

            }
        }

    }
}
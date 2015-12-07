package com.hw1.devlyn.thewateringhole1;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @Author: Devlyn Carroll
 * @Date: 4/8/2015.
 * MyApplicationClass contains the methods that have SQL preparedStatements to read in information
 * from the database tables, insert information into the tables or in some cases updated information
 * that already exists in the database.
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

        /**
         * This method is for when the user registers an account. The userName, userPswd and email
         * are inserted into the users DB table using the select and insert preparedStatements.
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

        /**
         * readDataBase method that loads the MySQL driver associated with the database. Sets up the
         * connection with the database to be able to read from it.
         * @throws Exception
         */
        public static void readDataBase() throws Exception {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Log.d("Before Connect", "Connecting to DB");
                connect = DriverManager
                        .getConnection(DBCON, USERNAME, USERPSWD);
                Log.d("Connected", "Reading DB");
            } catch (Exception e) {
                Log.e("MAC", "Error occured! Error was" + e.getMessage());
                e.getStackTrace();
                throw e;
            }

        }

        /**
         * loginUser method that reads in the userName and userPswd as parameters, selects the
         * userName and password from the users table and then executes the query. The values pulled
         * from the query are then assigned the ResultSet userResult and then checks to see if the
         * userResult does not have a next value. If it does then userResult is set to the value
         * of the userId.
         * @param userName
         * @param userPswd
         * @return
         */
        public int loginUser(String userName, String userPswd) {
            try {
                preparedStatement = connect
                        .prepareStatement("select * from users where userName=? and password=?");
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, userPswd);
                ResultSet userResult = preparedStatement.executeQuery();
                if (!userResult.next()) {
                    return -1;
                } else {
                    Log.d("CheckPoint", "UserResult" + userResult.getInt("idUsers"));
                    return userResult.getInt("idUsers");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }

        /**
         * userProfile method is used to get, set or update all the user's profile information such
         * as the userId, name, description, likes_dislikes and userImage from the database table
         * userProfile. Once the userId has been selected from the table the userProfileResult is
         * assigned the values that are retrieved from the database query. The userProfileResult is
         * then checked to see if there is another value. If there is not another value then the
         * user's profile information is inserted into the table where the userId was selected. If
         * there is more values then the profile information already exists and that profile
         * information is updated.
         * @param userId
         * @param name
         * @param description
         * @param likes_dislikes
         * @return
         */
        public int userProfile(String userId, String name, String description, String likes_dislikes/*, String userImage*/) {
            try {
                Log.d("UserProfile", "Got data: userId: " + userId + " desc: " + description + " likes-dislikes: " + likes_dislikes + "userName: " + name);
                preparedStatement = connect
                        .prepareStatement("select * from userProfile where userId=?");
                preparedStatement.setString(1, userId);
                Log.d("lol", "About to execute prepstatement1");
                ResultSet userProfileResult = preparedStatement.executeQuery();
                Log.d("SELECT", "inside select");
                if (!userProfileResult.next()) {

                    preparedStatement = connect
                            .prepareStatement("insert into userProfile (userName, description, likes_dislikes) values (?,?,?)");/*userImage*/
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, likes_dislikes);
                    //preparedStatement.setString(4, userImage);
                    Log.d("SQLConnect", "added to DB");
                    return preparedStatement.executeUpdate();
                } else {
                    preparedStatement = connect
                            .prepareStatement("update userProfile set userName=?, description=?, likes_dislikes=? where userId=?"); /*userImage=?*/

                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, description);
                    preparedStatement.setString(3, likes_dislikes);
                    //preparedStatement.setString(4, userImage);
                    preparedStatement.setString(4, userId);
                    Log.d("UPDATE", "userId already exists");
                    return preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }

        /**
         * getUserCoords method used to get the user's GPS coordinates(latitude,longitude) from the
         * database table userProfile. This is done by reading in the userId as a parameter and then
         * selecting the user from the table by the userId. The userLatitude & userLongitude are
         * also selected from the same row as the userId and then assigned to the ResultSet
         * userCoordsResult. Then if there is a next value in userCoordsResult those values are
         * assigned to userLatitude & userLongitude. Finally those doubles are added to the ArrayList
         * of doubles userCoords that is then returned for later use.
         * @param userId
         * @return
         */
        public ArrayList<Double> getUserCoords(int userId) {
            ArrayList<Double> userCoords = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select userLongitude, userLatitude from userProfile where userId in (select userId from userProfile where userId=?)");
                preparedStatement.setInt(1, userId);
                //Log.d("MyApplicationClass", "idUserProfile" + profileInfo.set(1, String.valueOf(userId)));
                ResultSet userCoordsResult = preparedStatement.executeQuery();
                userCoordsResult.next();

                //int userIdR = userCoordsResult.getInt(2);
                //double userLatitude = userCoordsResult.getDouble(2);//9
                double userLongitude = userCoordsResult.getDouble(1);//10
                double userLatitude = userCoordsResult.getDouble(2);//9

                //userCoords.add(0, Double.valueOf(userIdR));
                userCoords.add(0, userLongitude);
                userCoords.add(1, userLatitude);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userCoords;
        }

        /**
         * eventInfo method used to set, get or update all the eventInfo values such as; userId,
         * eventName, numParticipating, eventDescription. Those values are passed in as parameters
         * and the eventName is immediately used to select the row containing that eventName in the
         * database table events. eventInfoResult is then assigned the values from the query and
         * checked to see if it contains a next value. If it does not contain a next value then the
         * values are inserted into the table. If it does contain a next value then the event
         * already exists and we update the values for that event.
         * @param userId
         * @param eventName
         * @param numParticipating
         * @param eventDescription
         * @return
         */
        public int eventInfo(String userId, String eventName, String numParticipating, String eventDescription) {
            try {
                preparedStatement = connect
                        .prepareStatement("select * from events where eventName=?");
                preparedStatement.setString(1, eventName);
                Log.d("lol", "About to execute prepstatement1");
                ResultSet eventInfoResult = preparedStatement.executeQuery();
                Log.d("SELECT", "inside select");
                if (!eventInfoResult.next()) {

                    preparedStatement = connect
                            .prepareStatement("insert into events (eventName, numParticipating, description, userId) values (?,?,?,?)");
                    preparedStatement.setString(1, eventName);
                    preparedStatement.setString(2, numParticipating);
                    preparedStatement.setString(3, eventDescription);
                    preparedStatement.setString(4, userId);
                    Log.d("SQLConnect", "added to DB");
                    return preparedStatement.executeUpdate();
                } else {
                    preparedStatement = connect
                            .prepareStatement("update events set eventName=?, numParticipating=?, description=? where eventName=?");

                    preparedStatement.setString(1, eventName);
                    preparedStatement.setString(2, numParticipating);
                    preparedStatement.setString(3, eventDescription);
                    preparedStatement.setString(4, eventName);
                    //Log.d("UPDATE", "userId already exists");
                    return preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }

        /**
         * userEventInfo method that iteratively  checks to see if eventName, numParticipating and
         * description are in the database. This is based off the userId given and if the i count is
         * less than the numberOfColumns found, then each value from each column will be added to the
         * ArrayList of strings eventInfo, until there are no more values in the ResultSet.
         * getEventResult.
         * of columns in the database then
         * @param userId
         * @return
         */
        public ArrayList<String> userEventInfo(int userId) {
            ArrayList<String> eventInfo = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select eventName, numParticipating, description from events where userId in (select userId from events where userId=?)");//"select * from events where userId=?;");description, numParticipating
                preparedStatement.setInt(1, userId);
                ResultSet getEventResult = preparedStatement.executeQuery();
                //getEventResult.next();

                int numberOfColumns = getEventResult.getMetaData().getColumnCount();
                while (getEventResult.next()) {
                    int i = 1;
                    while(i <= numberOfColumns) {
                        eventInfo.add(getEventResult.getString(i++));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return eventInfo;
        }

        /**
         * userEventLocation method that reads in userId as a parameter. This selects the longitude,
         * latitude if they exists in the events table based off the userId. Then iteratively
         * checks the numberOfColumns within the table. Then the values found are added to the
         * ArrayList of strings eventLocation.
         * @param userId
         * @return
         */
        public ArrayList<String> userEventLocation(int userId) {
            ArrayList<String> eventLocation = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select  longitude, latitude from events where userId in (select userId from events where userId=?)");//"select * from events where userId=?;");description, numParticipating
                preparedStatement.setInt(1, userId);
                ResultSet getEventLocationResult = preparedStatement.executeQuery();
                //getEventResult.next();

                int numberOfColumns = getEventLocationResult.getMetaData().getColumnCount();
                while (getEventLocationResult.next()) {
                    int i = 1;
                    while(i <= numberOfColumns) {
                        eventLocation.add(getEventLocationResult.getString(i++));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return eventLocation;
        }

        /**
         * userEventTitles method that reads in the userId as a parameter. Then the method checks
         * to see if eventName is within the database. Then it checks to see if there is other
         * eventNames wherever the userId is. Then iteratively checks the numberOfColumns in the
         * table and adds the values to ArrayList of strings userEventTitles.
         * @param userId
         * @return
         */
        public ArrayList<String> userEventTitles(int userId) {
            ArrayList<String> userEventTitles = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select eventName from events where userId in (select userId from events where userId=?)");//"select * from events where userId=?;");description, numParticipating
                preparedStatement.setInt(1, userId);
                ResultSet getEventTitleResult = preparedStatement.executeQuery();
                //getEventResult.next();

                int numberOfColumns = getEventTitleResult.getMetaData().getColumnCount();
                while (getEventTitleResult.next()) {
                    int i = 1;
                    while(i <= numberOfColumns) {
                        userEventTitles.add(getEventTitleResult.getString(i++));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userEventTitles;
        }

        /**
         * getUserProfileInfo method that reads in userId as a paramter.
         * @param userId
         * @return
         */
        public ArrayList<String> getUserProfileInfo(int userId) {
            ArrayList<String> profileInfo = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select * from userProfile where userId=?;"/*and userId=? and description=? and likes_dislikes=?"*/);
                preparedStatement.setInt(1, userId);
                ResultSet getProfileResult = preparedStatement.executeQuery();
                getProfileResult.next();

                Log.d("MyApplication", "getProfileResult is :" + getProfileResult);

                int userIdR = getProfileResult.getInt(1);
                int idUserProfileR = getProfileResult.getInt(2);
                String userNameR = getProfileResult.getString(3);
                String descriptionR = getProfileResult.getString(4);
                String eventsR = getProfileResult.getString(5);
                String likes_dislikes_R = getProfileResult.getString(6);
                String userImageUri = getProfileResult.getString(7);


                profileInfo.add(0, String.valueOf(userIdR));
                profileInfo.add(1, String.valueOf(idUserProfileR));
                profileInfo.add(2, userNameR);
                profileInfo.add(3, descriptionR);
                profileInfo.add(4, eventsR);
                profileInfo.add(5, likes_dislikes_R);
                profileInfo.add(6, userImageUri);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return profileInfo;
        }

        public int addFriend(String userId, String friendEmail, String friendUserName) {
            try {
                ResultSet friendInfoResult;
                if(!friendEmail.isEmpty() && friendUserName.isEmpty()) {
                    preparedStatement = connect
                            .prepareStatement("select * from userProfile where userEmail=?");
                    preparedStatement.setString(1, friendEmail);
                    Log.d("lol", "About to execute prepstatement1");
                    friendInfoResult = preparedStatement.executeQuery();
                    Log.d("SELECT", "inside select");
                } else if (friendEmail.isEmpty() && !friendUserName.isEmpty()) {
                    preparedStatement = connect
                            .prepareStatement("select * from userProfile where userName=?");
                    preparedStatement.setString(1, friendUserName);
                    Log.d("lol", "About to execute prepstatement1");
                    friendInfoResult = preparedStatement.executeQuery();
                    Log.d("SELECT", "inside select");
                } else {
                    preparedStatement = connect
                            .prepareStatement("select * from userProfile where userName=? and userEmail=?");
                    preparedStatement.setString(1, friendUserName);
                    preparedStatement.setString(2, friendEmail);
                    Log.d("lol", "About to execute prepstatement1");
                     friendInfoResult = preparedStatement.executeQuery();
                    Log.d("SELECT", "inside select");
                }
                if(!friendInfoResult.next()){
                    return -3;
                }
                PreparedStatement preparedStatement2 = connect
                        .prepareStatement("select * from friends where userId=? and friendsWith=?");
                String friendUserId = friendInfoResult.getString(2);
                String friendLongitude = friendInfoResult.getString(10);
                String friendLatitude = friendInfoResult.getString(9);
                preparedStatement2.setString(1, userId);
                preparedStatement2.setString(2, friendUserId);
                ResultSet friendInfo = preparedStatement2.executeQuery();

                if (!friendInfo.next() ) {
                    preparedStatement = connect
                            .prepareStatement("insert into friends (userId, friendsWith, friendEmail, friendUserName, friendLatitude, friendLongitude) values (?,?,?,?,?,?)");
                    preparedStatement.setString(1, userId);
                    preparedStatement.setString(2, friendUserId);
                    preparedStatement.setString(3, friendEmail);
                    preparedStatement.setString(4, friendUserName);
                    preparedStatement.setString(5, friendLatitude);
                    preparedStatement.setString(6, friendLongitude);
                    Log.d("SQLConnect", "added to DB");
                    return preparedStatement.executeUpdate();
                } else {
                    //String friendUserId = friendInfoResult.getString(2);
                    preparedStatement = connect
                            .prepareStatement("update friends set friendsWith=?, friendEmail=?, friendUserName=? where userId=?");

                    return -2;//preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }

        public ArrayList<String> getFriendCoords(int userId) throws Exception {
            ArrayList<String> friendCoords = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select  friendUserName, friendLongitude, friendLatitude from friends where userId in (select userId from friends where userId=?)");
                preparedStatement.setInt(1, userId);
                Log.d("GetfriendCoords", "coords are:");
                ResultSet friendCoordsResult = preparedStatement.executeQuery();
                int numberOfColumns = friendCoordsResult.getMetaData().getColumnCount();
                while (friendCoordsResult.next()) {
                    int i = 1;
                    while(i <= numberOfColumns) {
                        friendCoords.add(friendCoordsResult.getString(i++));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return friendCoords;
        }

        public ArrayList<Double> setFriendCoords(String userId, double currentLongitude, double currentLatitude) {
            ArrayList<Double> currentLocation = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select * from friends where userId=?;");
                preparedStatement.setString(1, userId);

                ResultSet currentLocationR = preparedStatement.executeQuery();
                currentLocationR.next();

                if (currentLocationR.next()) {

                    preparedStatement = connect
                            .prepareStatement("insert into friends (friendLongitude, friendLatitude) values (?,?)");
                    preparedStatement.setDouble(1, currentLongitude);
                    preparedStatement.setDouble(2, currentLatitude);
                    preparedStatement.executeUpdate();
                    Log.d("SQLConnect", "added to DB");
                } else if(!currentLocationR.next()) {
                    preparedStatement = connect
                            .prepareStatement("update friends set friendLongitude=?, friendLatitude=? where idfriends=?");

                    preparedStatement.setDouble(1, currentLongitude);
                    preparedStatement.setDouble(2, currentLatitude);
                    preparedStatement.setString(3, userId);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return currentLocation;
        }
        public ArrayList<Double> setCurrentCoords(String userId, double currentLongitude, double currentLatitude) {
            ArrayList<Double> currentLocation = new ArrayList<>();
            try {
                preparedStatement = connect
                        .prepareStatement("select * from userProfile where userId=?;");
                preparedStatement.setString(1, userId);

                ResultSet currentLocationR = preparedStatement.executeQuery();
                currentLocationR.next();

                if (currentLocationR.next()) {

                    preparedStatement = connect
                            .prepareStatement("insert into userProfile (userLongitude, userLatitude) values (?,?)");
                    preparedStatement.setDouble(1, currentLongitude);
                    preparedStatement.setDouble(2, currentLatitude);
                    preparedStatement.executeUpdate();
                    Log.d("SQLConnect", "added to DB");
                } else if(!currentLocationR.next()) {
                    preparedStatement = connect
                            .prepareStatement("update userProfile set userLongitude=?, userLatitude=? where idUserProfile=?");

                    preparedStatement.setDouble(1, currentLongitude);
                    preparedStatement.setDouble(2, currentLatitude);
                    preparedStatement.setString(3, userId);
                    preparedStatement.executeUpdate();

                    preparedStatement = connect
                            .prepareStatement("update friends set friendLongitude=?, friendLatitude=? where friendsWith=?");
                    preparedStatement.setDouble(1, currentLongitude);
                    preparedStatement.setDouble(2, currentLatitude);
                    preparedStatement.setString(3, userId);
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return currentLocation;
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


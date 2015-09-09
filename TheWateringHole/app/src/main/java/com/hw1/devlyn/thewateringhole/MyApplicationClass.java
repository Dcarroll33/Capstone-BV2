package com.hw1.devlyn.thewateringhole;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by Devlyn on 4/8/2015.
 */
public class MyApplicationClass extends Application {

    public static class MySQLAccess {
        private static Connection connect = null;
        private static Statement statement = null;
        private static PreparedStatement preparedStatement = null;
        private static ResultSet resultSet = null;
        private static final String DBCON = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql575557";
        private static final String USERNAME = "sql575557";
        private static final String USERPSWD = "zP4%uP6!";

        public int registerUser(String userName, String userPswd, String email) {
            try {
                preparedStatement = connect
                        .prepareStatement("select * from Users where userName=?");
                preparedStatement.setString(1, userName);
                ResultSet userNameResult = preparedStatement.executeQuery();
                if (userNameResult.next()) {
                    return -2;
                }
                preparedStatement = connect
                        .prepareStatement("insert into Users (userName, password, email) values (?,?,?)");
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
                Log.d("Before Connect","Register");
                connect = DriverManager
                        .getConnection(DBCON, USERNAME, USERPSWD);
                Log.d("Connected","Register");
            } catch (Exception e) {
                e.getStackTrace();
                throw e;
            }

        }

        public int loginUser(String userName, String userPswd){
            try {
                preparedStatement = connect
                        .prepareStatement("select * from Users where userName=? and password=?");
                        preparedStatement.setString(1, userName);
                        preparedStatement.setString(2, userPswd);
                        ResultSet userResult =  preparedStatement.executeQuery();
                    if (!userResult.next()) {
                         return -1;
                    }else{
                        return userResult.getInt("idUsers");
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
                    connect.close();
                }
            } catch (Exception e) {

            }
        }

    }
}

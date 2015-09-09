package com.hw1.devlyn.thewateringhole;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Devlyn on 4/21/2015.
 */
public class ConnectDb extends AsyncTask<String, Void, Integer> {

    private Exception exception;

    private int result = -1;

    private int userId = -1;

    public static MyApplicationClass.MySQLAccess dao = new MyApplicationClass.MySQLAccess();

    public static MyApplicationClass.MySQLAccess getDao() {
        return dao;
    }

    protected Integer doInBackground(String... strings) {
        try {
            if (strings[0] == "register"){
                dao.readDataBase();
                    Log.d("conDb", "Registration success");
                    result = dao.registerUser(strings[1], strings[2], strings[3]);
                    return result;

            }else if(strings[0] == "login"){
                dao.readDataBase();
                int result = dao.loginUser(strings[1], strings[2]);
                if(result != -1){
                    Log.d("ConDB","Login successful! User ID is: " + result);
                    userId = result;
                    return 0;
                }
                else {
                    Log.d("ConDB","Login failed :'(");
                    return -1;
                }
            }
 } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }

    public int getResult(){
        return result;
    }

    public int getUserId(){
        return userId;
    }
}

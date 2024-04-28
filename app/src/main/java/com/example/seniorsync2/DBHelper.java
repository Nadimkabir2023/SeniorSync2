package com.example.seniorsync2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
// DBHelper class to handle all database operations for the app
public class DBHelper extends SQLiteOpenHelper {
    Context context;
    private static final String DATABASE_NAME = "Login.db";  // Database name
    private static final int DATABASE_VERSION = 1; // Database version
    // Constructor for DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase myDB) {
        // Create tables within the database
        myDB.execSQL("create Table users(username Text primary key, password Text)");
        myDB.execSQL("create Table userprofile(nickname TEXT primary key, " +
                "fullname TEXT not null, age TEXT not null, gender TEXT not null, email TEXT not null, address TEXT not null ," +
                " emergency_phone1 TEXT not null, emergency_phone2 TEXT not null, emergency_phone3 TEXT not null)");
        myDB.execSQL("create Table healthdata(c_time TEXT primary key, low_pressure Integer not null, high_pressure Integer not null, " +
                "heartbeat Integer not null, before_eat Integer not null, after_eat Integer not null)");
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {
        // create fresh ones
        myDB.execSQL("drop Table if exists users");
        myDB.execSQL("drop table if exists userprofile");
        myDB.execSQL("drop table if exists healthdata");
        onCreate(myDB);
    }

    // Method to insert new user data into the users table
    public Boolean insertData(String username, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = myDB.insert("users", null, contentValues);

        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }
    // Method to check if a username already exists in the users table
    public Boolean checkusername(String username) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select *from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    // Method to check if a username and password match for login
    public Boolean checkusernamePassword(String username, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // Method to insert new user profile data into the userprofile table
    public Boolean insert_userprofile_Data(String nickname, String fullname, String age, String gender, String email,
                                           String emergency_phone1, String emergency_phone2, String emergency_phone3,String address) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nickname", nickname);
        cv.put("fullname", fullname);
        cv.put("age", age);
        cv.put("gender", gender);
        cv.put("email", email);
        cv.put("emergency_phone1", emergency_phone1);
        cv.put("emergency_phone2", emergency_phone2);
        cv.put("emergency_phone3", emergency_phone3);
        cv.put("address", address);
        long result = myDB.insert("userprofile", null, cv);

        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }
    // Method to retrieve all user profile data from the userprofile table
    public Cursor getdata() {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("Select * from userprofile", null);
        return cursor;
    }

    // Method to add new health data into the healthdata table
    public Boolean addHealthInfo(String current_time, int low_pressure, int high_pressure, int heartbeat, int before_eat, int after_eat) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("c_time", current_time);
        cv.put("low_pressure", low_pressure);
        cv.put("high_pressure", high_pressure);
        cv.put("heartbeat", heartbeat);
        cv.put("before_eat", before_eat);
        cv.put("after_eat", after_eat);

        long result = myDB.insert("healthdata", null, cv);

        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    // Method to read all health data from the healthdata table
    public Cursor readAllData() {
        SQLiteDatabase myDB = this.getWritableDatabase();
        Cursor cursor = myDB.rawQuery("Select * from healthdata", null);
        return cursor;
    }

    // Updates the health data in the database for a given time. If the specified record exists, it updates the details and returns true if successful
    public Boolean UpdateHealthData(String current_time, String low_pressure, String high_pressure, String heartbeat, String before_eat, String after_eat){
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("low_pressure", low_pressure);
        contentValues.put("high_pressure", high_pressure);
        contentValues.put("heartbeat", heartbeat);
        contentValues.put("before_eat", before_eat);
        contentValues.put("after_eat", after_eat);
        Cursor cursor = myDB.rawQuery("Select * from healthdata where c_time = ?", new String[] {current_time});
        if(cursor.getCount() > 0) {
            long result = myDB.update("healthdata", contentValues, "c_time=?", new String[] {current_time});
            if(result == -1) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }

    }
    // Deletes a specific row from the healthdata table based on the given time. Returns true if the deletion is successful
    public Boolean deleteOneRow(String current_time) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        long result = myDB.delete("healthdata", "c_time=?", new String[]{current_time});
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }
    // Deletes all data from the healthdata table
    void deleteAllData(){
        SQLiteDatabase myDB = this.getWritableDatabase();
        myDB.execSQL("DELETE FROM " + "healthdata");
    }

}
package com.example.seniorsync2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
// Class definition extending AppCompatActivity to provide compatibility features for older versions of Android
public class User extends AppCompatActivity {
    // Declare a DrawerLayout to handle the navigation drawer
    DrawerLayout drawerLayout;
    // Declare a DBHelper object for database operations
    DBHelper myDB;
    // Declare TextViews for displaying user information
    TextView nickname, fullname, age, gender, email, address, emergency_phone1, emergency_phone2, emergency_phone3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view for this activity from a predefined XML layout
        setContentView(R.layout.activity_user);
        // Initialize the drawer layout and database helper
        drawerLayout = findViewById(R.id.drawer_layout);
        myDB = new DBHelper(this);
        savedata(); // Fetch and display the user data.
        // Initialize TextViews by finding them by their IDs
        nickname = findViewById(R.id.Tx_1);
        fullname = findViewById(R.id.Tx_2);
        age = findViewById(R.id.Tx_3);
        gender = findViewById(R.id.Tx_4);
        email = findViewById(R.id.Tx_5);
        address = findViewById(R.id.Tx_6);
        emergency_phone1 = findViewById(R.id.Tx_7);
        emergency_phone2 = findViewById(R.id.Tx_8);
        emergency_phone3 = findViewById(R.id.Tx_9);

    }

    // Method to handle the menu button click, opens the drawer
    public void ClickMenu(View view) {
        HomeActivity.openDrawer(drawerLayout); }

    // Method to handle the logo click, closes the drawer
    public void ClickLogo(View view) {
        HomeActivity.closeDrawer(drawerLayout);
    }

    // Method to redirect to the Dashboard activity
    public void ClickDashboard(View view) {
        HomeActivity.redirectActivity(this, ControlPanel.class);
    }

    // Method to redirect back to the User activity
    public void ClickAboutUs(View view) {
        HomeActivity.redirectActivity(this, User.class);
    }

    // Method to redirect to the Login activity on logout
    public void ClickLogout(View view) {
        HomeActivity.redirectActivity(this, LoginActivity.class);
    }

    // Method to exit the application
    public void ClickExit(View view) {
        HomeActivity.exit(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Ensure the drawer is closed when the activity is paused to prevent it from staying open
        HomeActivity.closeDrawer(drawerLayout);
    }

    // Method to retrieve and display data from the database
    public void savedata(){
        // Get user data from the database
        Cursor c = myDB.getdata();
        // Check if data retrieval was successful and move to the first row of data
        if(c.moveToFirst()) {
            String str1, str2, str3, str4, str5, str6, str7, str8, str9;
            // Retrieve each column's data
            str1 = c.getString(0); // nickname
            str2 = c.getString(1); // fullname
            str3 = c.getString(2); // age
            str4 = c.getString(3); // gender
            str5 = c.getString(4); // email
            str6 = c.getString(5); // address
            str7 = c.getString(6); // emergency phone 1
            str8 = c.getString(7); // emergency phone 2
            str9 = c.getString(8); // emergency phone 3
            // Link each string to its respective TextView and set the text
            TextView textView1 =  findViewById(R.id.Tx_1);
            TextView textView2 =  findViewById(R.id.Tx_2);
            TextView textView3 =  findViewById(R.id.Tx_3);
            TextView textView4 =  findViewById(R.id.Tx_4);
            TextView textView5 =  findViewById(R.id.Tx_5);
            TextView textView6 =  findViewById(R.id.Tx_6);
            TextView textView7 =  findViewById(R.id.Tx_7);
            TextView textView8 =  findViewById(R.id.Tx_8);
            TextView textView9 =  findViewById(R.id.Tx_9);
            textView1.setText(str1);
            textView2.setText(str2);
            textView3.setText(str3);
            textView4.setText(str4);
            textView5.setText(str5);
            textView6.setText(str6);
            textView7.setText(str7);
            textView8.setText(str8);
            textView9.setText(str9);
        }
    }


}
// Package declaration for the application
package com.example.seniorsync2;
// Importing necessary classes and libraries
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;

import java.util.Calendar;
// HomeActivity class that extends AppCompatActivity
public class HomeActivity extends AppCompatActivity {

    // Class variables
    int i = 0; // Used to cycle through emergency numbers
    DBHelper myDB; // Database helper for data access
    DrawerLayout drawerLayout; // Layout for navigation drawer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Sets the layout for this activity
        // Initialization of UI components and database helper
        drawerLayout = findViewById(R.id.drawer_layout);
        myDB = new DBHelper(this);
        // Setup of emergency call button and its click listener
        ImageButton emergency_btn = (ImageButton) findViewById(R.id.emergency_call);
        emergency_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor =  myDB.getdata(); // Fetch data from database
                if(cursor.moveToFirst()) {
                    // Fetching emergency numbers from the cursor
                    String emergency_num = cursor.getString(5);
                    String emergency_num1 = cursor.getString(6);
                    String emergency_num2 = cursor.getString(7);
                    // Cycle through emergency numbers
                    if(i == 0) {
                        emergency_call(emergency_num);
                        if (emergency_num1.length() != 0)i += 1;
                    }
                    else if(i == 1) {
                        emergency_call(emergency_num1);
                        if (emergency_num2.length() != 0)i += 1;
                        else i = 0;
                    }
                    else if(i == 2) {
                        emergency_call(emergency_num2);
                        i = 0;
                    }
                }
            }
        });

        // Setup notifications timing
        Calendar NotificationTime = Calendar.getInstance();
        Calendar CurrentTime = Calendar.getInstance();
        NotificationTime.set(Calendar.HOUR_OF_DAY, 8);
        NotificationTime.set(Calendar.MINUTE, 0);
        NotificationTime.set(Calendar.SECOND, 0);

        if(NotificationTime.before(CurrentTime)){
            NotificationTime.add(Calendar.HOUR_OF_DAY,24);
        }

        long DelayTime = NotificationTime.getTimeInMillis() - CurrentTime.getTimeInMillis();
        // Setting constraints for the background tasks
        Constraints.Builder builder = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED);

        Data.Builder data = new Data.Builder();
        // Request permissions for SMS
        ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

    }


    // Method to initiate an emergency call
    public void emergency_call(String emergency_num){
        Intent forcall = new Intent();
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
            forcall.setAction(Intent.ACTION_CALL);
            forcall.setData(Uri.parse("tel:999"));
            startActivity(forcall);
        }else{
            ActivityCompat.requestPermissions(this,new String []{
                    Manifest.permission.CALL_PHONE},PackageManager.PERMISSION_GRANTED);
        }
    }


    // UI interactions for drawer menu
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // Navigation methods
    public void ClickDashboard(View view) {
        redirectActivity(this, ControlPanel.class);
    }

    public void ClickAboutUs(View view) {
        redirectActivity(this, User.class);
    }

    public void ClickLogout(View view) {
        redirectActivity(this, LoginActivity.class);
    }

    public void ClickExit(View view) {
        exit(this);
    }

    // Method to handle application exit with confirmation dialog
    public static void exit(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.logout_title);
        builder.setMessage(R.string.logout_content);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finishAffinity();
                System.exit(0);
            }
        });

        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    // Method to redirect to another activity
    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    // Event handlers for various UI buttons leading to different activities
    public void btn_health_data(View view) {
        Intent intent = new Intent(this, Health_data_record.class);
        startActivity(intent);
    }

    public void set_alarm(View view) {
        Intent intent = new Intent(this, rMainMedREminder.class);
        startActivity(intent);
    }

    public void btn_health_monitor(View view) {
        // Create an Intent to start the VitalsActivity
        Intent intent = new Intent(this, mVitalsActivity.class);
        // Start the activity
        startActivity(intent);
    }

    public void btn_yoga(View view) {
        // Create an Intent to start the YogaMainActivity
        Intent intent = new Intent(this, yYogaMainActivity.class);
        // Start the activity
        startActivity(intent);
    }
}
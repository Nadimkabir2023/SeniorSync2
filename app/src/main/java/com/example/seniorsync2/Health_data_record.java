package com.example.seniorsync2;
// Importing necessary libraries and classes for the Android application
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
// Declaring the class for the health data record screen, extending AppCompatActivity for basic app functionality
public class Health_data_record extends AppCompatActivity {
    // Declaring variables for UI components and data handling
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    DBHelper myDB;
    ArrayList<String> current_time, low_pressure, high_pressure, heartbeat, before_eat, after_eat;
    hCustomAdapter customAdapter;
    ImageButton btn_delete_healthData, health_lineChart;

    // Method called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_health_data_record);
        // Linking UI components to variables
        recyclerView = findViewById(R.id.recyclerview);
        add_button = findViewById(R.id.add_button);
        btn_delete_healthData = findViewById(R.id.btn_delete_healthdata);
        health_lineChart = findViewById(R.id.health_lineChart);
        // Setting up a listener for the add button to start a new activity for adding health data
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Health_data_record.this, hAdd_health_info.class);
                startActivity(intent);
            }
        });
        // Initializing the database helper and arraylists to store health data
        myDB = new DBHelper(this);
        current_time = new ArrayList<>();
        low_pressure = new ArrayList<>();
        high_pressure = new ArrayList<>();
        heartbeat= new ArrayList<>();
        before_eat = new ArrayList<>();
        after_eat = new ArrayList<>();
        // Method to load data from the database into arrays
        storeDataInArrays();
        // Setting up the RecyclerView with a custom adapter and LinearLayoutManager
        customAdapter = new hCustomAdapter(Health_data_record.this, this, current_time, low_pressure, high_pressure, heartbeat, before_eat, after_eat);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Health_data_record.this));

        // Setting a listener for the delete button to show a confirmation dialog
        btn_delete_healthData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
        // Setting a listener for the chart button to navigate to the line chart screen
        health_lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Health_data_record.this, hData_lineChart.class);
                startActivity(i);
            }
        });


    }

    // Method to handle the result from the activity started for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            recreate();
        }
    }
    // Method to read data from the database and store it in array lists
    void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }
        else {
            while(cursor.moveToNext()) {
                current_time.add(cursor.getString(0));
                low_pressure.add(cursor.getString(1));
                high_pressure.add(cursor.getString(2));
                heartbeat.add(cursor.getString(3));
                before_eat.add(cursor.getString(4));
                after_eat.add(cursor.getString(5));
            }
        }
    }

    // Method to display a confirmation dialog for deleting all health data
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete all?");
        builder.setMessage("Are you sure you want to delete all data ?");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myDB.deleteAllData(); // Deletes all data from the database
                Intent intent = new Intent(Health_data_record.this, Health_data_record.class);
                startActivity(intent);  // Restarts the activity.
                finish();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
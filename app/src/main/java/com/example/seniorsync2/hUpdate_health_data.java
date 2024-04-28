package com.example.seniorsync2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
// Class definition for the activity that allows updating and deleting health data
public class hUpdate_health_data extends AppCompatActivity {
    // UI components
    TextView current_time;
    EditText low_pressure, high_pressure, heart_beat, before_eat, after_eat;
    Button btn_update_healthData, btn_delete;
    // Variables to store incoming data
    String time, low, high, beat, before, after;
    // Database helper
    DBHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_update_health_data);
        // Linking Java objects to UI elements
        current_time = findViewById(R.id.current_time_input2);
        low_pressure = findViewById(R.id.low_pressure_input2);
        high_pressure = findViewById(R.id.high_pressure_input2);
        heart_beat = findViewById(R.id.heartbeat_input2);
        before_eat = findViewById(R.id.before_eat_input2);
        after_eat = findViewById(R.id.after_eat_input2);
        btn_update_healthData = findViewById(R.id.btn_update_healthData);
        btn_delete = findViewById(R.id.btn_delete);
        // Initialize DBHelper
        myDB = new DBHelper(this);

        // Fetch and set the intent data in the form fields
        getAndSetIntentData();
        // Set listener for the update button
        btn_update_healthData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch data from EditText fields
                String current_timeTXT = current_time.getText().toString();
                String low_pressureTXT = low_pressure.getText().toString();
                String high_pressureTXT = high_pressure.getText().toString();
                String heart_beatTXT = heart_beat.getText().toString();
                String before_eatTXT = before_eat.getText().toString();
                String after_eatTXT = after_eat.getText().toString();

                // Call the database helper to update the data
                Boolean checkUpdateInfo = myDB.UpdateHealthData(current_timeTXT, low_pressureTXT, high_pressureTXT, heart_beatTXT, before_eatTXT, after_eatTXT);
                if(checkUpdateInfo == true) {
                    Toast.makeText(hUpdate_health_data.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                    // Redirect to the record listing page
                    Intent intent = new Intent(hUpdate_health_data.this, Health_data_record.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(hUpdate_health_data.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set listener for the delete button
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            } // Show confirmation dialog before deletion
        });

    }

    // Method to fetch data from the Intent and set it in the UI
    void getAndSetIntentData(){
        if(getIntent().hasExtra("time") && getIntent().hasExtra("low") && getIntent().hasExtra("high")
                && getIntent().hasExtra("beat") && getIntent().hasExtra("before") && getIntent().hasExtra("after")) {
            // Retrieving data from Intent
            time = getIntent().getStringExtra("time");
            low = getIntent().getStringExtra("low");
            high = getIntent().getStringExtra("high");
            beat = getIntent().getStringExtra("beat");
            before = getIntent().getStringExtra("before");
            after = getIntent().getStringExtra("after");

            // Setting data to UI components
            current_time.setText(time);
            low_pressure.setText(low);
            high_pressure.setText(high);
            heart_beat.setText(beat);
            before_eat.setText(before);
            after_eat.setText(after);

        }
        else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to display a confirmation dialog for deletion
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete it ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Attempt to delete the record
                Boolean checkDelete = myDB.deleteOneRow(time);
                if(checkDelete == true) {
                    Toast.makeText(hUpdate_health_data.this, "Delete Successful!", Toast.LENGTH_SHORT).show();
                    // Redirect to the health record listing page
                    Intent intent = new Intent(hUpdate_health_data.this, Health_data_record.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(hUpdate_health_data.this, "Delete Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}
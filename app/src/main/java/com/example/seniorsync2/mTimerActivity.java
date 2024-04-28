package com.example.seniorsync2;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
// Class definition that extends AppCompatActivity, a base class for activities that use features in the support library
public class mTimerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // Call the superclass method for creating the activity.
        setContentView(R.layout.m_activity_accelerometer); // Set the layout for this activity from a predefined XML file.

        String type = ""; // Initialize a string to store the type of measurement.
        Intent intent = getIntent(); // Get the intent that started this activity.
        if (intent != null)  // Check if the intent is not null.
            type = intent.getStringExtra("name"); // Retrieve the extra information passed with the intent

        TextView timer = findViewById(R.id.timer); // Get the TextView widget by its ID from the layout to show the timer

        String finalType = type; // Make a final copy of the type string for use in inner classes
        new CountDownTimer(5100, 1000) { // Create a new CountDownTimer object set for 5100 milliseconds with steps of 1000 milliseconds

            public void onTick(long millisUntilFinished) { // Method called at every tick of the timer
                timer.setText(Long.toString(millisUntilFinished/1000)); // Update the TextView with the seconds remaining
            }

            public void onFinish() { // Method called when the timer finishes
                timer.setText("0"); // Set the timer TextView to 0 indicating the end
                Intent intent; // Declare an Intent object to start a new activity
                switch (finalType) { // Switch on the type of measurement to decide which activity to start
                    case "spo2": // Case for SpO2 measurement
                      intent = new Intent(mTimerActivity.this, mMonitorActivity.class);
                      break;
                    case "bp": // Case for blood pressure measurement
                        intent = new Intent(mTimerActivity.this, mBloodPressureActivity.class);
                        break;
                    case "hr": // Case for heart rate measurement
                        intent = new Intent(mTimerActivity.this, mHeartBeatActivity.class);
                        break;
                    case "resp":  // Case for respiratory rate measurement
                        intent = new Intent(mTimerActivity.this, mAccelerometer.class);
                        break;
                    default: // Default case for any other or unspecified measurements.
                        intent = new Intent(mTimerActivity.this, mMonitorActivity.class);
                        break;
                }
                startActivity(intent); // Start the new activity based on the case
                finish(); // Finish and close the current activity
            }

        }.start(); // Start the countdown timer

    }
}
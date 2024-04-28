package com.example.seniorsync2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
// Define the class yCleanse that extends AppCompatActivity
public class yCleanse extends AppCompatActivity {
    // Declare an array to hold the resource IDs of the cleanse options
    int[] newArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the superclass method for creating the activity
        setContentView(R.layout.y_activity_cleanse); // Set the layout for this activity from a predefined XML file
        // Initialize the array with IDs representing different cleanse views in the layout
        newArray = new int[]{
                R.id.cleanse1, R.id.cleanse2, R.id.cleanse3, R.id.cleanse4, R.id.cleanse5, R.id.cleanse6, R.id.cleanse7
        };
    }
    // Method to handle click events on the cleanse views
    public void clicked(View view) {
        // Iterate through the array of IDs
        for (int i=0; i<newArray.length; i++){
            // Check if the ID of the clicked view matches the current ID in the array
            if(view.getId() == newArray[i]){
                // Calculate the position of the clicked item, incremented by one for display purposes
                int value = i+1;
                // Log the position of the clicked cleanse option to the debug console
                Log.i("FIRST", String.valueOf(value));
                // Create an intent to start a new activity, yOpen2, passing the position value
                Intent intent = new Intent(yCleanse.this, yOpen2.class);
                intent.putExtra("value", String.valueOf(value)); // Attach the position value to the intent
                startActivity(intent); // Start the activity specified by the intent
            }
        }
    }
}
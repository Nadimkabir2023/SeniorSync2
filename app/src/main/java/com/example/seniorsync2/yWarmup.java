package com.example.seniorsync2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
// Define the class yWarmup that extends AppCompatActivity
public class yWarmup extends AppCompatActivity {
    // Declare an array to store the resource IDs of the warm-up activities
    int[] newArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the superclass method for activity creation
        setContentView(R.layout.y_activity_warmup); // Set the layout for the activity from a predefined XML file
        // Initialize the array with IDs representing different warm-up views in the layout
        newArray = new int[]{
                R.id.warmup1, R.id.warmup2, R.id.warmup3, R.id.warmup4, R.id.warmup5, R.id.warmup6, R.id.warmup7,
                R.id.warmup8, R.id.warmup9, R.id.warmup10, R.id.warmup12
        }; // Note that R.id.warmup11 is missing, which might be an error or intentional
    }
    // Method to handle click events on the warm-up views
    public void clicked(View view) {
        // Iterate through the array of IDs
        for (int i=0; i<newArray.length; i++){
            // Check if the ID of the clicked view matches the current ID in the array
            if(view.getId() == newArray[i]){
                // Calculate the position of the clicked item, incremented by one for display purposes
                int value = i+1;
                // Log the position of the clicked warm-up to the debug console
                Log.i("FIRST", String.valueOf(value));
                // Create an intent to start a new activity, yOpen1, passing the position value
                Intent intent = new Intent(yWarmup.this, yOpen1.class);
                intent.putExtra("value", String.valueOf(value)); // Attach the position value to the intent
                startActivity(intent); // Start the activity specified by the intent
            }
        }
    }
}
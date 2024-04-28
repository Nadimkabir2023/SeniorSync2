package com.example.seniorsync2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
// Define the class yAsanas that extends AppCompatActivity
public class yAsanas extends AppCompatActivity {
    // Declare an array to hold resource IDs of the asanas
    int[] newArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the superclass method
        setContentView(R.layout.y_activity_asanas); // Set the content view to a specific layout representing the activity's UI
        // Initialize the array with IDs representing different asana views in the layout
        newArray = new int[]{
                R.id.asana1, R.id.asana2, R.id.asana3, R.id.asana4, R.id.asana5, R.id.asana6, R.id.asana7, R.id.asana8,
                R.id.asana9, R.id.asana10, R.id.asana11, R.id.asana12, R.id.asana13, R.id.asana14,
                R.id.asana15, R.id.asana16, R.id.asana17
        };
    }
    // Method to handle click events on the asana views
    public void clicked(View view) {
        // Iterate through the array of IDs
        for (int i=0; i<newArray.length; i++){
            // Check if the ID of the clicked view matches the current ID in the array
            if(view.getId() == newArray[i]){
                // Calculate the position of the clicked item, incremented by one for display purposes
                int value = i+1;
                // Log the position of the clicked asana to the debug console
                Log.i("FIRST", String.valueOf(value));
                // Create an intent to start a new activity, yOpen3, passing the position value
                Intent intent = new Intent(yAsanas.this, yOpen3.class);
                intent.putExtra("value", String.valueOf(value)); // Attach the position value to the intent
                startActivity(intent); // Start the activity specified by the intent
            }
        }
    }
}
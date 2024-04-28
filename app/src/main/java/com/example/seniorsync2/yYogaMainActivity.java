package com.example.seniorsync2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
// Class declaration extending AppCompatActivity
public class yYogaMainActivity extends AppCompatActivity {
    // Declare a variable for storing cautionary messages, though it's not initialized or used in this snippet
    String caution;
    // Declare a TextToSpeech object to handle spoken instructions or messages
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the superclass method for creating the activity
        setContentView(R.layout.y_activity_yogamain); // Set the content view for this activity to a specific XML layout file
        // Initialize the TextToSpeech object and define its initialization listener
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // Check if the initialization was successful without errors
                if (i != TextToSpeech.ERROR) {
                    // Set the language for the TextToSpeech engine to English
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }
    // Method to handle the warm-up button click
    public void warmup(View view) {
        // Create an intent to start the Warmup activity
        Intent intent = new Intent(yYogaMainActivity.this, yWarmup.class);
        startActivity(intent); // Start the Warmup activity
    }
    // Method to handle the cleansing button click
    public void cleansing(View view) {
        // Create an intent to start the Cleansing activity
        Intent intent = new Intent(yYogaMainActivity.this, yCleanse.class);
        startActivity(intent); // Start the Cleansing activity
    }
    // Method to handle the asanas button click
    public void asanas(View view) {
        // Create an intent to start the Asanas activity
        Intent intent = new Intent(yYogaMainActivity.this, yAsanas.class);
        startActivity(intent); // Start the Asanas activity
    }


}
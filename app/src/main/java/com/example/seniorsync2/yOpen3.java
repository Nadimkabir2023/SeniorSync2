package com.example.seniorsync2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

// Public class declaration that extends AppCompatActivity
public class yOpen3 extends AppCompatActivity {
    // Variable to store the incoming intent value indicating the current pose
    String buttonvalue;
    // Button to start and stop the countdown timer
    Button startBtn;
    // Timer to count down during each yoga pose
    private CountDownTimer countDownTimer;
    // TextView to display the remaining time
    TextView mtextview;
    // Flags to indicate if the timer is currently running
    private boolean MTimeRunning = false;
    // Remaining time in milliseconds.
    private  long MTimeLeftinmillis;
    // Buttons to control the text-to-speech output
    Button listenBtn;
    Button stopBtn;
    String description; // String to hold the description of the current yoga pose
    TextToSpeech textToSpeech;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.y_activity_open); // Set the content view to the specified XML layout
        // Retrieve value from the intent to determine which yoga pose to load.
        Intent intent  = getIntent();
        buttonvalue = intent.getStringExtra("value");
        int intvalue = Integer.valueOf(buttonvalue);
        // Switch statement to set the content view and description based on the pose number
        switch (intvalue){
            // Each case sets the layout for a specific yoga pose and loads the corresponding description
            case 1 :
                setContentView(R.layout.y_asana1);
                description = getString(R.string.apose1);
                break;
            case 2 :
                setContentView(R.layout.y_asana2);
                description = getString(R.string.apose2);
                break;
            case 3 :
                setContentView(R.layout.y_asana3);
                description = getString(R.string.apose3);
                break;
            case 4 :
                setContentView(R.layout.y_asana4);
                description = getString(R.string.apose4);
                break;
            case 5 :
                setContentView(R.layout.y_asana5);
                description = getString(R.string.apose5);
                break;
            case 6 :
                setContentView(R.layout.y_asana6);
                description = getString(R.string.apose6);
                break;
            case 7 :
                setContentView(R.layout.y_asana7);
                description = getString(R.string.apose7);
                break;
            case 8 :
                setContentView(R.layout.y_asana8);
                description = getString(R.string.apose8);
                break;
            case 9 :
                setContentView(R.layout.y_asana9);
                description = getString(R.string.apose9);
                break;
            case 11 :
                setContentView(R.layout.y_asana11);
                description = getString(R.string.apose11);
                break;
            case 13 :
                setContentView(R.layout.y_asana13);
                description = getString(R.string.apose13);
                break;
            case 14 :
                setContentView(R.layout.y_asana14);
                description = getString(R.string.apose14);
                break;
            case 15 :
                setContentView(R.layout.y_asana15);
                description = getString(R.string.apose15);
                break;
            case 17 :
                setContentView(R.layout.y_asana17);
                description = getString(R.string.apose17);
                break;
            case 18 :
                setContentView(R.layout.y_asana15);
                description = getString(R.string.apose18);
                break;
            case 24 :
                setContentView(R.layout.y_asana17);
                description = getString(R.string.apose24);
                break;
            // Additional cases for other poses
        }
        // Initialize UI components
        startBtn = findViewById(R.id.strtbtn);
        mtextview = findViewById(R.id.timer);
        // Event listener for the start/stop button
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MTimeRunning){
                    stopTimer(); // Stop the timer if it is running
                }
                else {
                    startTimer(); // Start the timer if it is not running
                }
            }
        });
        // Button to start text-to-speech playback
        listenBtn = findViewById(R.id.lstnBtn);
        stopBtn = findViewById(R.id.stopBtn);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the string resource for text-to-speech
                textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop the speech when the stop button is clicked
                if (textToSpeech != null && textToSpeech.isSpeaking()) {
                    textToSpeech.stop();
                }
            }
        });
    }
    private void stopTimer(){
        countDownTimer.cancel();
        MTimeRunning = false;
        startBtn.setText("START");
    }
    // Method to start the timer based on the value shown in the TextView
    private void startTimer(){
        final  CharSequence value1 = mtextview.getText();
        String num1 = value1.toString(); // Convert to string
        String num2 = num1.substring(0,2); // Extract minutes
        String num3 = num1.substring(3,5); // Extract seconds

        final  int number = Integer.valueOf(num2) * 60 + Integer.valueOf(num3);
        MTimeLeftinmillis = number * 1000;
        // Start countdown timer.
        countDownTimer = new CountDownTimer(MTimeLeftinmillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MTimeLeftinmillis = millisUntilFinished; // Update remaining time
                updateTimer(); // Update the timer display
            }

            @Override
            public void onFinish() {
                // Handle actions upon finishing the timer.
                if (textToSpeech != null) {
                    textToSpeech.stop(); // Stop any ongoing speech
                    textToSpeech.shutdown(); // Shutdown the TextToSpeech engine
                }
                int newvalue = Integer.valueOf(buttonvalue) + 1; // Increment the pose number
                if (newvalue <= 24){
                    // If not the last pose, start the next one
                    Intent intent = new Intent(yOpen3.this, yOpen3.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newvalue));
                    startActivity(intent);
                }
                else {
                    // If the last pose, start the activity over or go to another activity
                    newvalue = 1;
                    Intent intent = new Intent(yOpen3.this, yAsanas.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newvalue));
                    startActivity(intent);
                }
            }
        }.start();
        startBtn.setText("PAUSE");  // Change button text to "PAUSE"
        MTimeRunning = true; // Set the timer running state to true
    }

    // Method to update the display of the countdown timer
    private void updateTimer(){
        int minutes = (int) MTimeLeftinmillis / 600000; // Calculate minutes
        int seconds = (int) MTimeLeftinmillis % 60000 / 1000; // Calculate seconds
        String timeLeftText = ""; // Initialize display string
        if(minutes < 10) timeLeftText += "0";// Add leading zero if needed
        timeLeftText +=  minutes + ":"; // Add minutes to display string.
        if (seconds < 10) timeLeftText += "0";   // Add seconds to display string
        timeLeftText += seconds;
        mtextview.setText(timeLeftText);
    }
    // Override the onBackPressed method to properly handle the back button press
    @Override
    public  void onBackPressed() {
        if (textToSpeech != null) {
            textToSpeech.stop(); // Stop any ongoing text-to-speech.
            textToSpeech.shutdown();  // Properly shutdown the textToSpeech.
        }
        super.onBackPressed(); // Call the superclass method.
    }
}
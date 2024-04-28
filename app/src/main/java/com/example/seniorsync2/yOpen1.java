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

// Class declaration, extending AppCompatActivity
public class yOpen1 extends AppCompatActivity {
    // Holds the value passed from the previous activity to determine the yoga pose
    String buttonvalue;
    // Button to start and pause the timer
    Button startBtn;
    private CountDownTimer countDownTimer; // Timer to manage pose duration
    TextView mtextview; // Displays the countdown timer
    private boolean MTimeRunning = false; // Indicates whether the timer is currently running
    private  long MTimeLeftinmillis; // Stores the remaining time in milliseconds

    Button listenBtn; // Button to start text-to-speech
    Button stopBtn; // Button to stop text-to-speech
    String description; // Description of the current yoga pose
    TextToSpeech textToSpeech; // TextToSpeech object for reading the description aloud

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.y_activity_open); // Set the layout for the activity

        Intent intent  = getIntent(); // Get the intent that started this activity
        buttonvalue = intent.getStringExtra("value"); // Retrieve the value indicating the specific yoga pose
        int intvalue = Integer.valueOf(buttonvalue); // Convert that string value to an integer
        // Switch case to load specific content based on the pose number
        switch (intvalue){
            case 1 :
                setContentView(R.layout.y_warmup1);
                description = getString(R.string.wpose1);
                break;
            // Repeat cases for each yoga pose, setting the appropriate layout and description
            case 2 :
                setContentView(R.layout.y_warmup2);
                description = getString(R.string.wpose2);
                break;
            case 3 :
                setContentView(R.layout.y_warmup3);
                description = getString(R.string.wpose3);
                break;
            case 4 :
                setContentView(R.layout.y_warmup4);
                description = getString(R.string.wpose4);
                break;
            case 5 :
                setContentView(R.layout.y_warmup5);
                description = getString(R.string.wpose5);
                break;
            case 6 :
                setContentView(R.layout.y_warmup6);
                description = getString(R.string.wpose6);
                break;
            case 7 :
                setContentView(R.layout.y_warmup7);
                description = getString(R.string.wpose7);
                break;
            case 8 :
                setContentView(R.layout.y_warmup8);
                description = getString(R.string.wpose8);
                break;
            case 9 :
                setContentView(R.layout.y_warmup9);
                description = getString(R.string.wpose9);
                break;
            case 10 :
                setContentView(R.layout.y_warmup10);
                description = getString(R.string.wpose10);
                break;
            case 11 :
                setContentView(R.layout.y_warmup11);
                description = getString(R.string.wpose12);
                break;
            // Cases continue for each pose up to the maximum defined
        }

        startBtn = findViewById(R.id.strtbtn); // Initialize the start button
        mtextview = findViewById(R.id.timer); // Initialize the text view for displaying the timer
        // Set a click listener on the start button to control the timer
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MTimeRunning){
                    stopTimer(); // If timer is running, stop it
                }
                else {
                    startTimer(); // Otherwise, start the timer
                }
            }
        });
        // Initialize text-to-speech functionality and buttons for controlling it.
        listenBtn = findViewById(R.id.lstnBtn);
        stopBtn = findViewById(R.id.stopBtn);
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                // if No error is found then only it will run
                if (i != TextToSpeech.ERROR) {
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.ENGLISH); // Set the language to English
                }
            }
        });
        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use the string resource for speech
                textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop speech when the stop button is clicked
                if (textToSpeech != null && textToSpeech.isSpeaking()) {
                    textToSpeech.stop();
                }
            }
        });
    }
    // Method to stop the timer
    private void stopTimer(){
        countDownTimer.cancel(); // Cancel the countdown
        MTimeRunning = false; // Set the running flag to false
        startBtn.setText("START"); // Update the button text to indicate it can start the timer
    }
    // Method to start the timer
    private void startTimer(){
        final  CharSequence value1 = mtextview.getText(); // Get the current value from the timer TextView
        String num1 = value1.toString(); // Convert the CharSequence to String
        String num2 = num1.substring(0,2); // Get the minutes
        String num3 = num1.substring(3,5);

        final  int number = Integer.valueOf(num2) * 60 + Integer.valueOf(num3); // Convert time to total seconds
        MTimeLeftinmillis = number * 1000; // Convert seconds to milliseconds

        countDownTimer = new CountDownTimer(MTimeLeftinmillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MTimeLeftinmillis = millisUntilFinished; // Update the time left in milliseconds.
                updateTimer();
            }

            @Override
            public void onFinish() {
                if (textToSpeech != null) {
                    textToSpeech.stop(); // Stop any ongoing text-to-speech.
                    textToSpeech.shutdown(); // Shutdown the textToSpeech to release resources
                }
                int newvalue = Integer.valueOf(buttonvalue) + 1;
                if (newvalue <= 12){
                    Intent intent = new Intent(yOpen1.this, yOpen1.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newvalue));
                    startActivity(intent);
                }
                else {
                    newvalue = 1;
                    Intent intent = new Intent(yOpen1.this, yWarmup.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newvalue));// Pass the reset value
                    startActivity(intent); // Start the activity
                }
            }
        }.start(); // Start the countdown timer.
        startBtn.setText("PAUSE");  // Update the button text to "PAUSE"
        MTimeRunning = true; // Set the running flag to true
    }
    // Method to update the timer display in the TextView
    private void updateTimer(){
        int minutes = (int) MTimeLeftinmillis / 600000; // Calculate remaining minutes
        int seconds = (int) MTimeLeftinmillis % 60000 / 1000; // Calculate remaining seconds
        String timeLeftText = "";// Initialize the time display text
        if(minutes < 10) timeLeftText += "0"; // Format minutes to include a leading zero if needed
        timeLeftText +=  minutes + ":";// Append minutes to the display text.
        if (seconds < 10) timeLeftText += "0";// Format seconds to include a leading zero if needed
        timeLeftText += seconds; // Append seconds to the display text
        mtextview.setText(timeLeftText); // Set the updated time on the TextView
    }
    // Override the onBackPressed method to handle the back button press
    @Override
    public  void onBackPressed() {
        if (textToSpeech != null) {
            textToSpeech.stop(); // Stop any ongoing speech
            textToSpeech.shutdown(); // Shutdown the textToSpeech to release resources
        }
        super.onBackPressed(); // Call the superclass method
    }
}
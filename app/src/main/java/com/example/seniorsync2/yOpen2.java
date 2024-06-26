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

// Public class extending AppCompatActivity for basic activity functionalities
public class yOpen2 extends AppCompatActivity {
    // Variables to store data passed from previous activities or used in this activity
    String buttonvalue;
    Button startBtn;
    private CountDownTimer countDownTimer;
    TextView mtextview;
    private boolean MTimeRunning = false;
    private  long MTimeLeftinmillis;

    Button listenBtn;
    Button stopBtn;
    String description;
    TextToSpeech textToSpeech;
    // Method to initialize the activity
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.y_activity_open);
        // Get the intent that started this activity and extract the string.
        Intent intent  = getIntent();
        buttonvalue = intent.getStringExtra("value");
        int intvalue = Integer.valueOf(buttonvalue);
        // Switch statement to load different layouts and descriptions based on the 'intvalue'.
        switch (intvalue){
            case 1 :
                setContentView(R.layout.y_cleanse1);
                description = getString(R.string.cpose1);
                break;
            case 2 :
                setContentView(R.layout.y_cleanse2);
                description = getString(R.string.cpose2);
                break;
            case 3 :
                setContentView(R.layout.y_cleanse3);
                description = getString(R.string.cpose3);
                break;
            case 4 :
                setContentView(R.layout.y_cleanse4);
                description = getString(R.string.cpose4);
                break;
            case 5 :
                setContentView(R.layout.y_cleanse5);
                description = getString(R.string.cpose5);
                break;
            case 6 :
                setContentView(R.layout.y_cleanse6);
                description = getString(R.string.cpose6);
                break;
            case 7 :
                setContentView(R.layout.y_cleanse7);
                description = getString(R.string.cpose7);
                break;
        }
        // Initialize UI components
        startBtn = findViewById(R.id.strtbtn);
        mtextview = findViewById(R.id.timer);
        // Set up click listener for the start/pause button
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MTimeRunning){
                    stopTimer();
                }
                else {
                    startTimer();
                }
            }
        });
        // Set up Text-to-Speech for describing the cleanse
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
    // Method to stop the timer and update the button text
    private void stopTimer(){
        countDownTimer.cancel();
        MTimeRunning = false;
        startBtn.setText("START");
    }
    // Method to start the timer, calculate the total duration from the TextView
    private void startTimer(){
        final  CharSequence value1 = mtextview.getText();
        String num1 = value1.toString();
        String num2 = num1.substring(0,2);
        String num3 = num1.substring(3,5);

        final  int number = Integer.valueOf(num2) * 60 + Integer.valueOf(num3);
        MTimeLeftinmillis = number * 1000;

        countDownTimer = new CountDownTimer(MTimeLeftinmillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MTimeLeftinmillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                if (textToSpeech != null) {
                    textToSpeech.stop();
                    textToSpeech.shutdown();
                }
                int newvalue = Integer.valueOf(buttonvalue) + 1;
                if (newvalue <= 8){
                    Intent intent = new Intent(yOpen2.this, yOpen2.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newvalue));
                    startActivity(intent);
                }
                else {
                    newvalue = 1;
                    Intent intent = new Intent(yOpen2.this, yCleanse.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("value",String.valueOf(newvalue));
                    startActivity(intent);
                }
            }
        }.start();
        startBtn.setText("PAUSE");
        MTimeRunning = true;
    }
    // Method to update the timer display
    private void updateTimer(){
        int minutes = (int) MTimeLeftinmillis / 600000;
        int seconds = (int) MTimeLeftinmillis % 60000 / 1000;
        String timeLeftText = "";
        if(minutes < 10) timeLeftText += "0";
        timeLeftText +=  minutes + ":";
        if (seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;
        mtextview.setText(timeLeftText);
    }
    // Override the onBackPressed method to handle back button press
    @Override
    public  void onBackPressed() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onBackPressed();
    }
}
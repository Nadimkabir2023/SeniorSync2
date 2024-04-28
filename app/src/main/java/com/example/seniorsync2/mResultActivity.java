package com.example.seniorsync2;
// Import necessary Android and Firebase components
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
// Define the activity class that extends AppCompatActivity for compatibility features
public class mResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view for this activity to the specified XML layout
        setContentView(R.layout.m_activity_result);
        // Initialize TextViews by finding them by their ID in the layout
        TextView name = findViewById(R.id.name);
        TextView score = findViewById(R.id.scoreText);
        TextView normal = findViewById(R.id.normal);
        // Get the Intent that started this activity to retrieve passed data
        Intent intent = getIntent();
        if (intent != null) {
            // Set the text of the 'name' TextView to the string extra from the intent
            name.setText(intent.getStringExtra("name"));
            // Check if the passed name equals "Blood Pressure"
            if (intent.getStringExtra("name").equals("Blood Pressure"))
                // If true, set the score TextView directly from the intent's string extra
                score.setText(intent.getStringExtra("score"));
            else
                // If not "Blood Pressure", check if the score is not -1
                score.setText(intent.getIntExtra("score", 0) != -1 ? Integer.toString(intent.getIntExtra("score", 0)): "Insufficient data");

            normal.setText("Normal range\n" + intent.getStringExtra("normal"));
            // Set the text of the 'normal' TextView to "Normal range" followed by the normal range from the intent
            DatabaseReference database = FirebaseDatabase.getInstance("https://sih-raamen-default-rtdb.firebaseio.com/").getReference("username");
            // Create a new HashMap to store data that will be pushed to Firebase
            HashMap<String, Object> map = new HashMap<>();
            // Put current system time as "date" in the map
            map.put("date", System.currentTimeMillis());
            // Put the test type from the intent extra as "type" in the map
            map.put("type", intent.getStringExtra("name"));
            // Put the score from the intent extra as "score" in the map
            map.put("score", intent.getIntExtra("score", 0));
            // Push the map to Firebase under a child node named after the current system time
            database.child(Long.toString(System.currentTimeMillis())).setValue(map);
        }
    }
}
// Specifies the package within which the class resides.
package com.example.seniorsync2;
// Import statements for necessary Android classes
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
// Class definition for mVitalsActivity, handling the vitals selection screen
public class mVitalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets the content view layout for this activity from the predefined XML file
        setContentView(R.layout.m_activity_vitals);
        // Initializes CardView widgets for each vital measurement option
        CardView bp = findViewById(R.id.bp);
        CardView hr = findViewById(R.id.hr);
        CardView resp = findViewById(R.id.resp);
        // Sets an onClickListener for the BP CardView to start the timer activity with "bp" as an extra parameter
        bp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mVitalsActivity.this, mTimerActivity.class);
                intent.putExtra("name", "bp"); // Put extra data in the Intent to indicate it is for BP
                startActivity(intent); // Start the activity as specified by the Intent
            }
        });
        // Sets an onClickListener for the HR CardView to start the timer activity with "hr" as an extra parameter
        hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mVitalsActivity.this, mTimerActivity.class); // Create an Intent to start the mTimerActivity
                intent.putExtra("name", "hr"); // Put extra data in the Intent to indicate it is for HR
                startActivity(intent); // Start the activity as specified by the Intent
            }
        });
        // Sets an onClickListener for the Resp CardView to start the timer activity with "resp" as an extra parameter
        resp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mVitalsActivity.this, mTimerActivity.class); // Create an Intent to start the mTimerActivity
                intent.putExtra("name", "resp");  // Put extra data in the Intent to indicate it is for Respiratory Rate
                startActivity(intent); // Start the activity as specified by the Intent
            }
        });
    }
}
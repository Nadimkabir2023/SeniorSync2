package com.example.seniorsync2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
// Define the class Userprofile_initialize, which extends AppCompatActivity
public class Userprofile_initialize extends AppCompatActivity {

    // Declare EditText variables for user input fields
    EditText nickname, fullname, age, gender,email, emergency_phone1, emergency_phone2, emergency_phone3, address;
    Button save_data; // Declare a Button for the save action
    DBHelper myDB; // Declare an instance of DBHelper to interact with the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view for this activity from a predefined XML layout file
        setContentView(R.layout.activity_userprofile_initialize);

        // Initialize EditText fields by finding them by their IDs from the layout
        nickname = findViewById(R.id.nickname);
        fullname = findViewById(R.id.fullname);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        email = findViewById(R.id.email);
        emergency_phone1 = findViewById(R.id.emergency_phone1);
        emergency_phone2 = findViewById(R.id.emergency_phone2);
        emergency_phone3 = findViewById(R.id.emergency_phone3);
        address = findViewById(R.id.address);
        // Initialize the save button from the layout and the DBHelper object
        save_data = findViewById(R.id.btn_save_data);
        myDB = new DBHelper(this);
        // Set an onClickListener for the save button
        save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve text from EditText fields and convert to String
                String nicknameTXT = nickname.getText().toString();
                String fullnameTXT = fullname.getText().toString();
                String ageTXT = age.getText().toString();
                String genderTXT = gender.getText().toString();
                String emailTXT = email.getText().toString();
                String addressTXT = address.getText().toString();
                String emergency_phone1TXT = emergency_phone1.getText().toString();
                String emergency_phone2TXT = emergency_phone2.getText().toString();
                String emergency_phone3TXT = emergency_phone3.getText().toString();

                // Call the insert_userprofile_Data method of DBHelper to insert the data into the database
                Boolean checkInsertData = myDB.insert_userprofile_Data(nicknameTXT, fullnameTXT, ageTXT, genderTXT, emailTXT,
                        emergency_phone1TXT, emergency_phone2TXT,emergency_phone3TXT, addressTXT);
                // Check the result of the insert operation and display a Toast message accordingly
                if(checkInsertData == true){
                    Toast.makeText(Userprofile_initialize.this, R.string.insert_success, Toast.LENGTH_SHORT).show();
                    // If data insertion is successful, start the LoginActivity
                    Intent i = new Intent(Userprofile_initialize.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Userprofile_initialize.this, R.string.insert_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
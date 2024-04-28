package com.example.seniorsync2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
// Define the activity class that extends AppCompatActivity
public class RegisterActivity extends AppCompatActivity {

    // Declare variables for user input fields and a button
    EditText username, password, repassword;
    Button btnSignUp;
    DBHelper myDB; // Declare a DBHelper object for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity from a predefined XML file
        setContentView(R.layout.register_main);

        // Initialize EditText fields by finding them by ID from the layout
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        repassword = (EditText)findViewById(R.id.repassword);
        // Initialize the sign up button from the layout
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        // Instantiate the database helper class with the activity context
        myDB = new DBHelper(this);
        // Set an onClickListener for the sign-up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text entered by the user in EditText fields
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                // Check if any of the fields are empty
                if(user.equals("") || pass.equals("") || repass.equals(""))
                {   // Display a message prompting to fill all fields if any field is empty
                    Toast.makeText(RegisterActivity.this, "Fill all the fields.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Check if the password and repeated password match
                    if(pass.equals(repass))
                    {
                        // Check if the username is already taken
                        Boolean usercheckResult = myDB.checkusername(user);
                        if(usercheckResult == false)
                        {
                            // Insert new user data into the database if username is available
                            Boolean reResult = myDB.insertData(user,pass);
                            if(reResult == true)
                            {
                                // Display success message and navigate to user profile initialization activity
                                Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Userprofile_initialize.class);
                                startActivity(intent);
                            }
                            else
                            {
                                // Display error message if registration failed
                                Toast.makeText(RegisterActivity.this, R.string.register_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            // Display message if username already exists
                            Toast.makeText(RegisterActivity.this, R.string.user_exit, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        // Display message if passwords do not match
                        Toast.makeText(RegisterActivity.this, R.string.password_match, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
// Package declaration specific to the project
package com.example.seniorsync2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
// LoginActivity class definition, extending AppCompatActivity for app compatibility support
public class LoginActivity extends AppCompatActivity {

    // UI components and other class members declaration
    EditText username, password;
    DBHelper myDB; // Database helper object for database operations
    Button bt_login, bt_register; // Buttons for login and registration actions
    String t1; // Temporary string variable
    private CheckBox remember_pass; // Checkbox for "Remember Password" option
    private CheckBox auto_login;  // Checkbox for "Auto Login" option
    private SharedPreferences loginPreference; // SharedPreferences to store user preferences

    // onCreate method is the entry point for activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Setting the content view to the activity_login layout
        // Initialization of variables and UI components
        t1 = "25r";
        bt_register = findViewById(R.id.btnRegister);
        bt_login = findViewById(R.id.btnLogin);
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        remember_pass = (CheckBox) findViewById(R.id.RememberPass);
        auto_login = (CheckBox) findViewById(R.id.Auto_login);

        myDB = new DBHelper(this); // Creating an instance of DBHelper
        // Setup SharedPreferences for storing login preferences
        loginPreference = getSharedPreferences("login", MODE_PRIVATE);
        // Check and apply stored login preferences if available
        boolean checked = loginPreference.getBoolean("checked", false);
        if (checked) {
            Map<String, Object> m = readLogin();
            if (m != null) {
                username.setText((CharSequence) m.get("userName"));
                password.setText((CharSequence) m.get("password"));
                remember_pass.setChecked(true);
            }

            if (loginPreference.getBoolean("AUTO_isCHECK", false)) {
                auto_login.setChecked(true);
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
            }


        }

        // Setting onClickListener for the login button
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                // Check for empty fields and attempt login
                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.login_ps_success, Toast.LENGTH_SHORT).show();
                } else {
                    Boolean result = myDB.checkusernamePassword(user, pass);
                    if (result == true) {
                        configLoginInfo(remember_pass.isChecked());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.login_ps_fail, Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });


        // Setting onClickListener for the registration button
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        // Setting onCheckedChangeListener for the auto_login checkbox
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("Login Successful");
                    loginPreference.edit().putBoolean("AUTO_isCHECK", true).commit();
                } else {
                    System.out.println("Incorrect Username or Password");
                    loginPreference.edit().putBoolean("AUTO_isCHECK", false).commit();
                }
            }
        });

    }
    // Method to configure login information based on user choice
    public void configLoginInfo(boolean checked) {
        SharedPreferences.Editor editor = loginPreference.edit();
        editor.putBoolean("checked", remember_pass.isChecked());

        if (checked) {
            editor.putString("userName", username.getText().toString());
            editor.putString("password", password.getText().toString());
        } else {
            editor.remove("userName").remove("password");
        }
        editor.commit();
    }

    // Method to read login information from SharedPreferences
    private Map<String, Object> readLogin() {
        Map<String, Object> m = new HashMap<>();
        String userName = loginPreference.getString("userName", "");
        String password = loginPreference.getString("password", "");
        m.put("userName", userName);
        m.put("password", password);
        return m;
    }
}
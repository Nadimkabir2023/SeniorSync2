package com.example.seniorsync2;
// Required imports for the Android framework classes used.
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
// Definition of the ControlPanel class which extends AppCompatActivity, a base class for activities.
public class ControlPanel extends AppCompatActivity {
        // Declaration of a DrawerLayout object to manage a drawer view
        DrawerLayout drawerLayout;
        // onCreate is called when the activity is starting.
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Sets the user interface layout for this Activity
            setContentView(R.layout.activity_controlpanel);
            // Initialize the drawerLayout by finding the View by its ID in the layout resources.
            drawerLayout = findViewById(R.id.drawer_layout);
        }
        // Method to handle click events on the menu icon.
        public void ClickMenu(View view) {
            // Calls the static method openDrawer from HomeActivity to open the drawer
            HomeActivity.openDrawer(drawerLayout);
        }

        // Method to handle click events on the logo icon
        public void ClickLogo(View view) {
            // Calls the static method closeDrawer from HomeActivity to close the drawer
            HomeActivity.closeDrawer(drawerLayout);
        }
        // Method to handle click events on the dashboard menu item
        public void ClickDashboard(View view) {
            // Calls the recreate method to re-create the activity to refresh its content
            recreate();
        }
        // Method to handle click events on the about us menu item
        public void ClickAboutUs(View view) {
            // Calls the static method redirectActivity from HomeActivity to start the User activity
            HomeActivity.redirectActivity(this, User.class);
        }
        // Method to handle click events on the logout menu item
        public void ClickLogout(View view) {
            // Calls the static method redirectActivity from HomeActivity to start the LoginActivity
            HomeActivity.redirectActivity(this, LoginActivity.class);
        }
        // Method to handle click events on the exit menu item
        public void ClickExit(View view) {
            // Calls the static method exit from HomeActivity to exit the application
            HomeActivity.exit(this);
        }
        // onPause is called when the activity is going into the background
        @Override
        protected void onPause() {
            super.onPause();
            // Ensures that the drawer is closed when the activity goes into the background
            HomeActivity.closeDrawer(drawerLayout);
        }

}
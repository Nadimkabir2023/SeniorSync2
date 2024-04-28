package com.example.seniorsync2;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

// Custom Application class that runs when the application starts
public class rMedReminder extends Application {
  // Unique identifier for the notification channel
  public static final String CHANNEL_ID = "notification_channel";

  // Method called when the application is created
  @Override
  public void onCreate() {
    super.onCreate();
    // Method to create notification channels
    setupNotificationChannels();
  }

  // Method to create notification channels
  private void setupNotificationChannels() {
    // Check if Android version is 8.0 (Oreo) or higher
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      // Create a new notification channel
      NotificationChannel notificationChannel = new NotificationChannel(
              CHANNEL_ID,
              "Notification Channel for MedManager",
              NotificationManager.IMPORTANCE_HIGH
      );
      notificationChannel.setDescription("Notifications for MedManager are shown here");  // Description of the channel

      // Get the NotificationManager
      NotificationManager notificationManager = getSystemService(NotificationManager.class);

      // Register the notification channel with the notification manager
      notificationManager.createNotificationChannel(notificationChannel);
    }
  }
}

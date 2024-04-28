package com.example.seniorsync2;

import static com.example.seniorsync2.rMedReminder.CHANNEL_ID;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

// This class extends BroadcastReceiver to handle alarm events
public class rMedicationReminderReceiver extends BroadcastReceiver {
    private NotificationManagerCompat notificationManager;
    // Variable to track notification IDs
    public static int notificationId = 1;

    // This method is invoked when an alarm is triggered
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieve medication and user details from the intent
        String medicationName = intent.getStringExtra("medName");
        String medicationQuantity = intent.getStringExtra("medQty");

        // Setup the notification manager and send the notification
        notificationManager = NotificationManagerCompat.from(context);
        triggerNotification(context, "Please take your " + medicationQuantity + " dose(s) of " + medicationName + ".", intent);
    }

    // Method to send notification to the specified channel
    public void triggerNotification(Context context, String message, Intent intent) {
        // Create the notification
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_ss)  // Small icon for the notification
                .setContentTitle("Medication Reminder")  // Notification title
                .setContentText(message)  // Notification content
                .setPriority(NotificationCompat.PRIORITY_HIGH)  // Priority of the notification
                .setCategory(NotificationCompat.CATEGORY_ALARM)  // Category of the notification
                .setAutoCancel(true)  // Auto-cancel when the notification is tapped
                .build();

        // Post the notification
        notificationManager.notify(notificationId++, notification);
    }
}

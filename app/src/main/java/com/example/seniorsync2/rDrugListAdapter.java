package com.example.seniorsync2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seniorsync2.data.MedReminderDB;

import java.util.Calendar;

// Adapter for the RecyclerView managing the list of medications
public class rDrugListAdapter extends RecyclerView.Adapter {
    // Cursor holding the medication data
    private Cursor drugListCursor;
    // Context of the application
    public Context context;
    // Helper class for database operations
    public MedReminderDB dbHelper;

    // Constructor for initializing the adapter
    public rDrugListAdapter(Context context, MedReminderDB helper) {
        this.context = context;
        this.dbHelper = helper;
    }

    // Method to update the data set
    public void refreshData(Cursor cursor) {
        this.drugListCursor = cursor;
        if (drugListCursor != null) {
            drugListCursor.moveToFirst();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DrugHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creating a new ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_card_layout, parent, false);
        return new DrugHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (drugListCursor != null) {
            // Fill the ViewHolder with appropriate data
            ((DrugHolder) holder).medName.setText(drugListCursor.getString(1));
            ((DrugHolder) holder).quantity.setText("Quantity: " + drugListCursor.getInt(2));
            ((DrugHolder) holder).id = drugListCursor.getInt(0);
            ((DrugHolder) holder).time.setText("Time: "+drugListCursor.getString(3));

            // Setting the state of the toggle switch based on the medication status
            boolean isChecked = drugListCursor.getInt(5) == 1;
            ((DrugHolder) holder).toggleSwitch.setChecked(isChecked);

            // Setting up a listener for the toggle switch
            ((DrugHolder) holder).toggleSwitch.setOnClickListener(v -> {
                // Update the medication status
                if (((DrugHolder) holder).toggleSwitch.isChecked()) {
                    dbHelper.updateMedicineEnabled(dbHelper.getWritableDatabase(), ((DrugHolder) holder).id, 1);
                } else {
                    dbHelper.updateMedicineEnabled(dbHelper.getWritableDatabase(), ((DrugHolder) holder).id, 0);
                }

                // Setup a notification for the alarm using the updated information
                Cursor currentMed = dbHelper.fetchMedicineById(dbHelper.getWritableDatabase(), ((DrugHolder) holder).id);
                currentMed.moveToFirst();
                String[] timeComponents = currentMed.getString(3).split(":");
                int hour = Integer.parseInt(timeComponents[0]);
                int minute = Integer.parseInt(timeComponents[1]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar currentTime = Calendar.getInstance();
                currentTime.set(Calendar.SECOND, 0);
                currentTime.set(Calendar.MILLISECOND, 0);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, rMedicationReminderReceiver.class);
                intent.putExtra("medName", currentMed.getString(1));
                intent.putExtra("medQty", currentMed.getString(2));

                // Check if the medication reminder should be repeated
                String repeatDays = currentMed.getString(4);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ((DrugHolder) holder).id, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                if (repeatDays.equals("0000000")) {
                    // If no specific day is set, schedule for the next day
                    if (calendar.before(currentTime)) {
                        calendar.add(Calendar.DATE, 1);
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                } else {
                    // Schedule repeated alarms for each selected day of the week
                    int dayIndex = 1;
                    for (char day : repeatDays.toCharArray()) {
                        if (day == '1') {
                            calendar.set(Calendar.DAY_OF_WEEK, dayIndex);
                            if (calendar.before(currentTime)) {
                                calendar.add(Calendar.DATE, 7);
                            }
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                        }
                        dayIndex++;
                    }
                }
            });

            // Listener for deleting a medication
            ((DrugHolder) holder).deleteMed.setOnClickListener(v -> {
                // Perform the deletion operation
                dbHelper.removeMedicine(dbHelper.getWritableDatabase(), ((DrugHolder) holder).id);
                // Refresh the data set and update the RecyclerView
                refreshData(dbHelper.fetchAllMedicines(dbHelper.getWritableDatabase()));
            });
            // Move to the next item in the cursor
            drugListCursor.moveToNext();
        }
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the adapter
        return drugListCursor.getCount();
    }

    // ViewHolder class for holding medication item views
    public class DrugHolder extends RecyclerView.ViewHolder {
        TextView medName, time, quantity;
        ImageButton deleteMed;
        int id;
        Switch toggleSwitch;

        // Constructor for initializing the ViewHolder
        public DrugHolder(@NonNull View itemView) {
            super(itemView);
            // Assigning the elements within the ViewHolder
            medName = itemView.findViewById(R.id.txtMedicineName);
            time = itemView.findViewById(R.id.txtTime);
            quantity = itemView.findViewById(R.id.txtQuantity);
            deleteMed = itemView.findViewById(R.id.btnDelete);
            toggleSwitch = itemView.findViewById(R.id.toggle_switch);
        }
    }
}

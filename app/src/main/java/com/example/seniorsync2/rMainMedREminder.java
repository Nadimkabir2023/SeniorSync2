package com.example.seniorsync2;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seniorsync2.data.MedReminderDB;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// Class definition for RegisterActivity, handling the user registration process.
public class rMainMedREminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private RecyclerView medicationListRecyclerView;
    private rDrugListAdapter medicationAdapter;
    private FloatingActionButton addMedicationFab;
    private Button selectTimeButton;
    private EditText medicationNameInput, medicationQuantityInput;
    private Switch repeatMedicationSwitch;
    private ChipGroup daySelectionChipGroup;
    private Chip mondayChip, tuesdayChip, wednesdayChip, thursdayChip, fridayChip, saturdayChip, sundayChip;

    private MedReminderDB databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity from a predefined XML file
        setContentView(R.layout.drug_management_layout);

        // Initialize the database helper instance
        databaseHelper = MedReminderDB.getInstance(getApplicationContext());

        // Link the views from the layout
        medicationListRecyclerView = findViewById(R.id.drug_list_recycler);
        addMedicationFab = findViewById(R.id.add_drug_fab);

        // Set up the RecyclerView with a LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        medicationListRecyclerView.setLayoutManager(linearLayoutManager);

        // Initialize the adapter for the RecyclerView and set it
        medicationAdapter = new rDrugListAdapter(getApplicationContext(), databaseHelper);
        medicationAdapter.refreshData(databaseHelper.fetchAllMedicines(databaseHelper.getWritableDatabase()));
        medicationListRecyclerView.setAdapter(medicationAdapter);

        // Set up the listener for the FloatingActionButton to show the add medication dialog
        addMedicationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Method that displays the medication addition dialog
                medicineAdder().show();
            }
        });
    }

    /*
     * Creates and shows the dialog for adding new medication.
     */
    private AlertDialog medicineAdder() {
        // Defining the layout for the dialog content
        View layout = View.inflate(this, R.layout.drug_add_dialog, null);

        // Medication details
        medicationNameInput  = layout.findViewById(R.id.add_med_name);
        medicationQuantityInput = layout.findViewById(R.id.add_med_qty);
        selectTimeButton = layout.findViewById(R.id.add_med_time);
        // UI elements
        repeatMedicationSwitch = layout.findViewById(R.id.repeat_switch);
        daySelectionChipGroup = layout.findViewById(R.id.day_selection_group);
        toggleChildrenActivation(daySelectionChipGroup, false);
        mondayChip = layout.findViewById(R.id.monday_chip);
        tuesdayChip = layout.findViewById(R.id.tuesday_chip);
        wednesdayChip = layout.findViewById(R.id.wednesday_chip);
        thursdayChip = layout.findViewById(R.id.thursday_chip);
        fridayChip = layout.findViewById(R.id.friday_chip);
        saturdayChip = layout.findViewById(R.id.saturday_chip);
        sundayChip = layout.findViewById(R.id.sunday_chip);

        // Listener for selecting time
        selectTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new rClockPickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        // Listener for the repeat medication switch
        repeatMedicationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If repeat is enabled, activate the days, otherwise disable them
                if (!repeatMedicationSwitch.isChecked()) {
                    toggleChildrenActivation(daySelectionChipGroup, false);
                } else {
                    toggleChildrenActivation(daySelectionChipGroup, true);
                }
            }
        });

        // AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);  // Assigning the layout to the AlertDialog

        // Actions to be performed when the "ADD" button is clicked
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Convert the medication quantity from string to integer
                int qty = 0;
                if (!"".equals(medicationQuantityInput.getText().toString()))
                    qty = Integer.parseInt(medicationQuantityInput.getText().toString());

                // Determine the format of the days
                String days = "0000000";
                if (repeatMedicationSwitch.isChecked()) {
                    days = formatDaySelection(mondayChip, tuesdayChip, wednesdayChip, thursdayChip, fridayChip, saturdayChip, sundayChip);
                }

                // Execute the medication addition
                databaseHelper.insertMedicine(databaseHelper.getWritableDatabase(),medicationNameInput.getText().toString(), qty, selectTimeButton.getText().toString(), days);

                // Update the medication list and refresh the RecyclerView
                medicationAdapter.refreshData(databaseHelper.fetchAllMedicines(databaseHelper.getWritableDatabase()));
                medicationAdapter.notifyDataSetChanged();
                medicationListRecyclerView.setAdapter(medicationAdapter);
            }
        });

        // Actions to be performed when the "CANCEL" button is clicked
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();  // Returning the created AlertDialog
    }

    /*
     * Formats the selected days into a string pattern.
     */
    public String formatDaySelection(Chip monday, Chip tuesday, Chip wednesday, Chip thursday, Chip friday, Chip saturday, Chip sunday) {
        String dayString = "" + (monday.isChecked() ? "1" : "0") + (tuesday.isChecked() ? "1" : "0") + (wednesday.isChecked() ? "1" : "0") + (thursday.isChecked() ? "1" : "0") + (friday.isChecked() ? "1" : "0") + (saturday.isChecked() ? "1" : "0") + (sunday.isChecked() ? "1" : "0");
        return dayString;
    }

    /*
     * Enables or disables children within a ChipGroup based on the specified boolean.
     */

    public void toggleChildrenActivation(ChipGroup chipGroup, Boolean activate) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            chipGroup.getChildAt(i).setEnabled(activate);
        }
    }

    /*
     * Callback method to be invoked when the time is set.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Seçilen saati ekrana yaz
        selectTimeButton.setText(hourOfDay + ":" + minute);
    }

}
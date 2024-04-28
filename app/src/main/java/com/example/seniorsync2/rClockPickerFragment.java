package com.example.seniorsync2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// This fragment displays a dialog for time selection.
public class rClockPickerFragment extends DialogFragment {

    // This method is called to create the dialog when the fragment is initialized.
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Retrieve the current date and time information.
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // Obtain the current hour.
        int currentMinute = calendar.get(Calendar.MINUTE);     // Obtain the current minute.

        // Return a new instance of TimePickerDialog.
        // getActivity() returns the activity associated with this fragment.
        // OnTimeSetListener will trigger when a time is set.
        // DateFormat.is24HourFormat(getActivity()) checks if the 24-hour format is enabled.
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), currentHour, currentMinute, DateFormat.is24HourFormat(getActivity()));
    }
}

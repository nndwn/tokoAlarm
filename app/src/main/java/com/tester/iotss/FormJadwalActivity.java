package com.tester.iotss;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class FormJadwalActivity extends AppCompatActivity {

    private TextInputEditText startTimeEditText, endTimeEditText;
    private MaterialTimePicker startTimePicker, endTimePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_jadwal);

        AutoCompleteTextView exposedDropdownAutoComplete = findViewById(R.id.filledExposedDropdownAutoComplete);
        TextInputLayout textInputLayout = findViewById(R.id.filledExposedDropdown);

        // Create an ArrayAdapter with your list of items
        String[] items = getResources().getStringArray(R.array.feelings); // Define your items in strings.xml
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);

        // Set the adapter to the AutoCompleteTextView
        exposedDropdownAutoComplete.setAdapter(adapter);

        // Set threshold for minimum number of characters to show suggestions
        exposedDropdownAutoComplete.setThreshold(1); // Set this to show suggestions from the first character typed

        // Optional: Set an item click listener if you want to handle item clicks
        exposedDropdownAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);
            // Handle item selection here
        });


        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);

        startTimeEditText.setOnClickListener(v -> showTimePicker(true));
        endTimeEditText.setOnClickListener(v -> showTimePicker(false));
    }

    private void showTimePicker(boolean isStartTime) {
        int hour, minute;
        if (isStartTime) {
            hour = startTimePicker != null ? startTimePicker.getHour() : 12;
            minute = startTimePicker != null ? startTimePicker.getMinute() : 0;
        } else {
            hour = endTimePicker != null ? endTimePicker.getHour() : 12;
            minute = endTimePicker != null ? endTimePicker.getMinute() : 0;
        }

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hour)
                .setMinute(minute);

        MaterialTimePicker timePickerDialog = builder.build();

        timePickerDialog.addOnPositiveButtonClickListener(view -> {
            int selectedHour = timePickerDialog.getHour();
            int selectedMinute = timePickerDialog.getMinute();
            updateTime(isStartTime, selectedHour, selectedMinute);
        });

        if (isStartTime) {
            startTimePicker = timePickerDialog;
        } else {
            endTimePicker = timePickerDialog;
        }

        timePickerDialog.show(getSupportFragmentManager(), "tag");
    }

    private void updateTime(boolean isStartTime, int hour, int minute) {
        String time = String.format("%02d:%02d", hour, minute);
        if (isStartTime) {
            startTimeEditText.setText(time);
        } else {
            endTimeEditText.setText(time);
        }
    }
}


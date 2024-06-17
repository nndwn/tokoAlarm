package com.tester.iotss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.tester.iotss.data.model.Perangkat;
import com.tester.iotss.data.remote.api.ApiService;
import com.tester.iotss.data.remote.network.RetrofitClient;
import com.tester.iotss.data.remote.request.PerangkatRequest;
import com.tester.iotss.data.remote.response.PerangkatResponse;
import com.tester.iotss.databinding.ActivityFormJadwalBinding;
import com.tester.iotss.databinding.FragmentScheduleBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormJadwalActivity extends AppCompatActivity {

    private ActivityFormJadwalBinding formJadwalBinding;
    private TextInputEditText startTimeEditText, endTimeEditText;
    private MaterialTimePicker startTimePicker, endTimePicker;

    private ApiService apiService;
    private AutoCompleteTextView exposedDropdownAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        formJadwalBinding = ActivityFormJadwalBinding.inflate(getLayoutInflater());
        setContentView(formJadwalBinding.getRoot());

        MaterialToolbar toolbar = formJadwalBinding.topAppBar;
        toolbar.setNavigationOnClickListener(v -> finish());


        apiService = RetrofitClient.getClient().create(ApiService.class);
        exposedDropdownAutoComplete = findViewById(R.id.filledExposedDropdownAutoComplete);

        fetchPerangkatData("082210262266");

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

    private void fetchPerangkatData(String phoneNumber) {
        PerangkatRequest request = new PerangkatRequest(phoneNumber);

        Call<PerangkatResponse> call = apiService.fetchPerangkat(request);
        call.enqueue(new Callback<PerangkatResponse>() {
            @Override
            public void onResponse(Call<PerangkatResponse> call, Response<PerangkatResponse> response) {
                if (response.isSuccessful()) {
                    List<Perangkat> perangkatList = response.body().getData();
                  //  storePerangkatInDatabase(perangkatList);
                    setupDropdownAdapter(perangkatList);
                } else {
                    Toast.makeText(FormJadwalActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PerangkatResponse> call, Throwable t) {
                Toast.makeText(FormJadwalActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*
    private void storePerangkatInDatabase(List<Perangkat> perangkatList) {
        List<Perangkat> perangkatEntities = new ArrayList<>();
        for (Perangkat perangkat : perangkatList) {
            Perangkat entity = new Perangkat(
                    perangkat.getId_alat(),
                    perangkat.getNama_alat(),
                    perangkat.getTanggal_mulai(),
                    perangkat.getTanggal_selesai(),
                    perangkat.getSisa_hari()
            );
            perangkatEntities.add(entity);
        }

        // Insert into Room database in a background thread
        new Thread(() -> {
            appDatabase.perangkatDao().deleteAllPerangkat(); // Clear old data
            appDatabase.perangkatDao().insertAll(perangkatEntities); // Insert new data
        }).start();
    }*/

    private void setupDropdownAdapter(List<Perangkat> perangkatList) {
        List<String> namaAlatList = new ArrayList<>();
        for (Perangkat perangkat : perangkatList) {
            namaAlatList.add(perangkat.getNama_alat());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, namaAlatList);

        exposedDropdownAutoComplete.setAdapter(adapter);
        exposedDropdownAutoComplete.setThreshold(1); // Show suggestions from the first character typed

        // Optional: Set an item click listener if you want to handle item clicks
        exposedDropdownAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);
            // Handle item selection here
            Toast.makeText(FormJadwalActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
    }
}


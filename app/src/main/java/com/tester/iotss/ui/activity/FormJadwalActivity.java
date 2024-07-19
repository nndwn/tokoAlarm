package com.tester.iotss.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.tester.iotss.R;
import com.tester.iotss.domain.model.Perangkat;
import com.tester.iotss.domain.model.Schedule;
import com.tester.iotss.data.remote.api.ApiService;
import com.tester.iotss.data.remote.network.RetrofitClient;
import com.tester.iotss.data.remote.request.PerangkatRequest;
import com.tester.iotss.data.remote.request.ScheduleRequest;
import com.tester.iotss.data.remote.response.CommonApiResponse;
import com.tester.iotss.data.remote.response.PerangkatResponse;
import com.tester.iotss.databinding.ActivityFormJadwalBinding;
import com.tester.iotss.ui.fragment.ScheduleFragment;
import com.tester.iotss.utils.Common;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormJadwalActivity extends AppCompatActivity {
    private boolean isUpdated = false;

    private ActivityFormJadwalBinding formJadwalBinding;

    private Schedule schedule = null;

    private MaterialTimePicker startTimePicker, endTimePicker;

    private ApiService apiService;

    private List<Perangkat> perangkatList;

    private int selectedIdDevice = 0;
    private String startTime, endTime, days;
    private int isActive = 1;
    private ChipGroup chipGroupDays;
    List<String> selectedDays;
    String name = "";
    int isSensorSwitchActive = 0;
    int isSensorOhmActive = 0;
    int isSensorRfActive = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formJadwalBinding = ActivityFormJadwalBinding.inflate(getLayoutInflater());
        View rootView = formJadwalBinding.getRoot();
        setContentView(rootView);

        isUpdatedData();

        apiService = RetrofitClient.getClient().create(ApiService.class);

        setComponentListener();

        fetchPerangkatData(Common.sessionLogin.getNohp(this));

        setSupportActionBar(formJadwalBinding.topAppBar);

        formJadwalBinding.topAppBar.setNavigationOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        formJadwalBinding.refreshButton.setOnClickListener(v -> {
            getTextRule();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_form_jadwal, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.hapus_jadwal);
        if (isUpdated) {
            item.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.hapus_jadwal) {// Handle the click on "Hapus Jadwal" menu item
            DeleteJadwal();
            return true;
            // Add more cases for other menu items if needed
        }
        return super.onOptionsItemSelected(item);
    }

    private void isUpdatedData() {
        // Retrieve Schedule object from intent extras
        schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        if (schedule != null) {
            selectedIdDevice = Integer.parseInt(schedule.getId_book());
            formJadwalBinding.topAppBar.setTitle("Jadwal");
            formJadwalBinding.startTimeEditText.setText(schedule.getStart_time());
            formJadwalBinding.endTimeEditText.setText(schedule.getEnd_time());
            if (!schedule.getNama_paket().equals("-")) {
                formJadwalBinding.dropdownPerangkat.setText(schedule.getNama_paket());
            } else {
                formJadwalBinding.dropdownPerangkat.setText(schedule.getId_alat());
            }

            if (schedule.getSensor_switch().equals("1")) {
                formJadwalBinding.chipSensor1.setChecked(true);
            }

            if (schedule.getSensor_ohm().equals("1")) {
                formJadwalBinding.chipSensor2.setChecked(true);
            }

            if (schedule.getSensor_rf().equals("1")) {
                formJadwalBinding.chipSensor3.setChecked(true);
            }

            formJadwalBinding.chipSensor1.setText("Sensor Switch");
            formJadwalBinding.chipSensor2.setText("Sensor Ohm");
            formJadwalBinding.chipSensor3.setText("Sensor RF");

            formJadwalBinding.lnModulSensor.setVisibility(View.VISIBLE);
            formJadwalBinding.cvInformasiJadwal.setVisibility(View.VISIBLE);
            formJadwalBinding.btnSimpan.setVisibility(View.VISIBLE);


            String[] numbersArray = schedule.getDays().split(",");

            for (int i = 0; i < numbersArray.length; i++) {
                int index = Integer.parseInt(numbersArray[i]) - 1;
                View view = formJadwalBinding.chipGroupDays.getChildAt(index);
                if (view instanceof Chip) {
                    Chip chip = (Chip) view;
                    chip.setChecked(true);
                }
            }

            isUpdated = true;

            getTextRule();
        }
    }

    private void setComponentListener() {
        formJadwalBinding.topAppBar.setNavigationOnClickListener(v -> finish());
        formJadwalBinding.startTimeEditText.setOnClickListener(v -> showTimePicker(true, "Waktu Mulai"));
        formJadwalBinding.endTimeEditText.setOnClickListener(v -> showTimePicker(false, "Waktu Selesai"));

        formJadwalBinding.btnSimpan.setOnClickListener(v -> {
            if (!isUpdated) {
                AddNewJadwal();
            } else {
                UpdateExistingJadwal();
            }
        });

        chipGroupDays = formJadwalBinding.chipGroupDays;
    }

    private void showTimePicker(boolean isStartTime, String title) {
        int hour, minute;
        if (title == null)
            title = "Pilih Waktu";
        if (isStartTime) {
            hour = startTimePicker != null ? startTimePicker.getHour() : 12;
            minute = startTimePicker != null ? startTimePicker.getMinute() : 0;
        } else {
            hour = endTimePicker != null ? endTimePicker.getHour() : 12;
            minute = endTimePicker != null ? endTimePicker.getMinute() : 0;
        }

        MaterialTimePicker.Builder builder = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText(title)
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
        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hour, minute);
        if (isStartTime) {
            formJadwalBinding.startTimeEditText.setText(time);
        } else {
            formJadwalBinding.endTimeEditText.setText(time);
        }
    }

    private void fetchPerangkatData(String phoneNumber) {
        PerangkatRequest request = new PerangkatRequest(phoneNumber);

        Call<PerangkatResponse> call = apiService.fetchPerangkat(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<PerangkatResponse> call, Response<PerangkatResponse> response) {
                if (response.isSuccessful()) {
                    perangkatList = response.body().getData();
                    setupDropdownAdapter(perangkatList);
                }
            }

            @Override
            public void onFailure(Call<PerangkatResponse> call, Throwable t) {
                Toast.makeText(FormJadwalActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDropdownAdapter(List<Perangkat> perangkatList) {
        List<String> namaAlatList = new ArrayList<>();
        for (Perangkat perangkat : perangkatList) {
            namaAlatList.add(perangkat.getNama_alat() + " (" + perangkat.getId_alat() + ")");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, namaAlatList);

        formJadwalBinding.dropdownPerangkat.setAdapter(adapter);
        formJadwalBinding.dropdownPerangkat.setThreshold(1);

        // Optional: Set an item click listener if you want to handle item clicks
        formJadwalBinding.dropdownPerangkat.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter.getItem(position);

            selectedIdDevice = perangkatList.get(position).getId();

            formJadwalBinding.chipSensor1.setText(perangkatList.get(position).getNama_sensor_1());
            formJadwalBinding.chipSensor2.setText(perangkatList.get(position).getNama_sensor_2());
            formJadwalBinding.chipSensor3.setText(perangkatList.get(position).getNama_sensor_3());

            formJadwalBinding.lnModulSensor.setVisibility(View.VISIBLE);
            formJadwalBinding.cvInformasiJadwal.setVisibility(View.VISIBLE);
            formJadwalBinding.btnSimpan.setVisibility(View.VISIBLE);
        });
    }

    private void AddNewJadwal() {
        startTime = formJadwalBinding.startTimeEditText.getText().toString();
        endTime = formJadwalBinding.endTimeEditText.getText().toString();
        name = formJadwalBinding.dropdownPerangkat.getText().toString();

        days = convertListToString(getSelectedDays());

        if (formJadwalBinding.chipSensor1.isChecked()) {
            isSensorSwitchActive = 1;
        }

        if (formJadwalBinding.chipSensor2.isChecked()) {
            isSensorOhmActive = 1;
        }

        if (formJadwalBinding.chipSensor3.isChecked()) {
            isSensorRfActive = 1;
        }

        if (days.isEmpty()) {
            Toast.makeText(this, "Silahkan pilih hari", Toast.LENGTH_SHORT).show();
        } else {
            ScheduleRequest request = new ScheduleRequest(selectedIdDevice, name, startTime, endTime, days, isActive, isSensorSwitchActive, isSensorOhmActive, isSensorRfActive);

            Call<CommonApiResponse> call = apiService.sendSchedule(request);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<CommonApiResponse> call, Response<CommonApiResponse> response) {
                    if (response.isSuccessful()) {
                        CommonApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            String message = apiResponse.getMessage();
                            Toast.makeText(FormJadwalActivity.this, message, Toast.LENGTH_SHORT).show();
                            Common.scheduleFragment.refreshData();
                            finish();
                        }
                    } else {
                        Toast.makeText(FormJadwalActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommonApiResponse> call, Throwable t) {
                    Toast.makeText(FormJadwalActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void UpdateExistingJadwal() {
        startTime = formJadwalBinding.startTimeEditText.getText().toString();
        endTime = formJadwalBinding.endTimeEditText.getText().toString();
        name = formJadwalBinding.dropdownPerangkat.getText().toString();

        days = convertListToString(getSelectedDays());

        if (formJadwalBinding.chipSensor1.isChecked()) {
            isSensorSwitchActive = 1;
        }

        if (formJadwalBinding.chipSensor2.isChecked()) {
            isSensorOhmActive = 1;
        }

        if (formJadwalBinding.chipSensor3.isChecked()) {
            isSensorRfActive = 1;
        }

        if (days.isEmpty()) {
            Toast.makeText(this, "Silahkan pilih hari", Toast.LENGTH_SHORT).show();
        } else {
            ScheduleRequest request = new ScheduleRequest(selectedIdDevice, name, startTime, endTime, days, isActive, isSensorSwitchActive, isSensorOhmActive, isSensorRfActive);
            request.setId(schedule.getId());
            Call<CommonApiResponse> call = apiService.updateSchedule(request);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<CommonApiResponse> call, Response<CommonApiResponse> response) {
                    if (response.isSuccessful()) {
                        CommonApiResponse apiResponse = response.body();
                        if (apiResponse != null) {
                            String message = apiResponse.getMessage();
                            Toast.makeText(FormJadwalActivity.this, message, Toast.LENGTH_SHORT).show();
                            Common.scheduleFragment.refreshData();
                            finish();
                        }
                    } else {
                        Toast.makeText(FormJadwalActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CommonApiResponse> call, Throwable t) {
                    Toast.makeText(FormJadwalActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void DeleteJadwal() {
        ScheduleRequest request = new ScheduleRequest();
        request.setId(schedule.getId());
        Call<CommonApiResponse> call = apiService.deleteSchedule(request);
        call.enqueue(new Callback<>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<CommonApiResponse> call, Response<CommonApiResponse> response) {
                if (response.isSuccessful()) {
                    CommonApiResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        String message = apiResponse.getMessage();
                        Toast.makeText(FormJadwalActivity.this, message, Toast.LENGTH_SHORT).show();
                        Common.scheduleFragment.refreshData();
                        finish();
                    }
                } else {
                    Toast.makeText(FormJadwalActivity.this, "Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonApiResponse> call, Throwable t) {
                Toast.makeText(FormJadwalActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<String> getSelectedDays() {
        List<String> selectedDays = new ArrayList<>();
        for (int i = 0; i < formJadwalBinding.chipGroupDays.getChildCount(); i++) {
            View view = formJadwalBinding.chipGroupDays.getChildAt(i);
            if (view instanceof Chip) {
                Chip chip = (Chip) view;
                if (chip.isChecked()) {
                    selectedDays.add(getDayNumber(chip.getText().toString())); // Convert day name to number if needed
                }
            }
        }
        return selectedDays;
    }

    private String getDayNumber(String dayName) {
        return switch (dayName.toLowerCase()) {
            case "senin" -> "1";
            case "selasa" -> "2";
            case "rabu" -> "3";
            case "kamis" -> "4";
            case "jumat" -> "5";
            case "sabtu" -> "6";
            case "minggu" -> "7";
            default -> ""; // Handle unknown day names if necessary
        };
    }

    private String convertListToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private void getTextRule(){
        String textRule = "Aturan Jadwal\n\nSensor:";
        if(formJadwalBinding.chipSensor1.isChecked()){
            textRule += "\n- Switch (Aktif)";
        }else{
            textRule += "\n- Switch (Tidak Aktif)";
        }
        if(formJadwalBinding.chipSensor2.isChecked()){
            textRule += "\n- Ohm (Aktif)";
        }else{
            textRule += "\n- Ohm (Tidak Aktif)";
        }

        if(formJadwalBinding.chipSensor3.isChecked()){
            textRule += "\n- Radio Frequency (RF) (Aktif)";
        }else{
            textRule += "\n- Radio Frequency (RF) (Tidak Aktif)";
        }

        textRule += "\n\nPerangkat:\n";

        textRule += formJadwalBinding.dropdownPerangkat.getText().toString();

        textRule += "\n\nJadwal mulai:\nHari ";

        for (int i = 0; i < formJadwalBinding.chipGroupDays.getChildCount(); i++) {
            View view = formJadwalBinding.chipGroupDays.getChildAt(i);
            if (view instanceof Chip) {
                Chip chip = (Chip) view;
                if (chip.isChecked()) {
                    textRule += chip.getText().toString();
                    if(i != formJadwalBinding.chipGroupDays.getChildCount() -1){
                        textRule += ", ";
                    }
                }
            }
        }

        textRule += " pada pukul ";

        textRule += formJadwalBinding.startTimeEditText.getText();

        textRule += " s/d ";

        textRule += formJadwalBinding.endTimeEditText.getText();

        formJadwalBinding.tvRule.setText(textRule);
    }
}


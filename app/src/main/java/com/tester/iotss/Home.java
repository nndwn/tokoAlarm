package com.tester.iotss;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.RadioButton;

import com.tester.iotss.Fragment.FragmentAccount;
import com.tester.iotss.Fragment.FragmentListSubscriber;
import com.tester.iotss.Fragment.FragmentHome;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
public class Home extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        showPermissionRationaleDialog();
                    } else {
                        openAppSettings();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new FragmentListSubscriber()).commit();

        sharedPreferences = getSharedPreferences("NotificationPermission", MODE_PRIVATE);
        checkNotificationPermission();
    }

    @OnClick({R.id.rbHome, R.id.rbAccount,R.id.rbPesanan})
    public void onRadioButtonClicked(RadioButton radioButton) {
        boolean checked = radioButton.isChecked(); // Is the button now checked?
        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.rbHome:
                if (checked) {
                    // 1 clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentHome()).commit();
                }
                break;
            case R.id.rbAccount:
                if (checked) {
                    // 2 clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentAccount()).commit();
                }
                break;
            case R.id.rbPesanan:
                if (checked) {
                    // 2 clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentListSubscriber()).commit();
                }
                break;
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (sharedPreferences.getBoolean("PermissionDenied", false)) {
                    // User denied permission before, show rationale dialog or open app settings
                    if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                        showPermissionRationaleDialog();
                    } else {
                        openAppSettings();
                    }
                } else {
                    // Permission not requested before, request it
                    requestNotificationPermission();
                }
            }
        }
    }

    private void showPermissionRationaleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Izin Pemberitahuan Diperlukan")
                .setMessage("Aplikasi ini membutuhkan izin pemberitahuan untuk tetap menampilkan situasi terkini. Mohon berikan izin pemberitahuan.")
                .setPositiveButton("OK", (dialog, which) -> requestNotificationPermission())
                .setNegativeButton("Batal", (dialog, which) -> {
                    Toast.makeText(this, "Izin pemberitahuan ditolak. Silakan aktifkan di pengaturan.", Toast.LENGTH_LONG).show();
                    sharedPreferences.edit().putBoolean("PermissionDenied", true).apply();
                })
                .show();
    }

    private void requestNotificationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


}

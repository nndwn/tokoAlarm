package com.tester.iotss.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.tester.iotss.R;
import com.tester.iotss.ui.fragment.FragmentAccount;
import com.tester.iotss.ui.fragment.FragmentListSubscriber;
import com.tester.iotss.ui.fragment.FragmentHome;
import com.tester.iotss.ui.fragment.ScheduleFragment;
import com.tester.iotss.databinding.ActivityHomeBinding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

public class Home extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private ActivityHomeBinding activityHomeBinding;
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                            showPermissionRationaleDialog();
                        } else {
                            openAppSettings();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("NotificationPermission", MODE_PRIVATE);
        checkNotificationPermission();

        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(activityHomeBinding.getRoot());
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, new FragmentHome())
                .commit();

        activityHomeBinding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentHome()).commit();
                        return true;
                    case R.id.menu_monitoring:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentListSubscriber()).commit();
                        return true;
                    case R.id.menu_schedule:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ScheduleFragment()).commit();
                        return true;
                    case R.id.menu_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentAccount()).commit();
                        return true;
                }
                return false;
            }
        });

        ColorStateList iconColorStateList = ContextCompat.getColorStateList(this, R.color.bottom_nav_icon_selector);
        activityHomeBinding.bottomNavigation.setItemIconTintList(iconColorStateList);
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

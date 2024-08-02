package com.tester.iotss.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationBarView;
import com.tester.iotss.R;
import com.tester.iotss.databinding.ActivityHomeBinding;
import com.tester.iotss.ui.fragment.FragmentAccount;
import com.tester.iotss.ui.fragment.FragmentHome;
import com.tester.iotss.ui.fragment.FragmentListSubscriber;
import com.tester.iotss.ui.fragment.ScheduleFragment;

import butterknife.ButterKnife;

public class Home extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
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

        com.tester.iotss.databinding.ActivityHomeBinding activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(activityHomeBinding.getRoot());
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, new FragmentHome())
                .commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }


        activityHomeBinding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return switch (menuItem.getItemId()) {
                    case R.id.menu_home -> {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentHome()).commit();
                        yield true;
                    }
                    case R.id.menu_monitoring -> {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentListSubscriber()).commit();
                        yield true;
                    }
                    case R.id.menu_schedule -> {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ScheduleFragment()).commit();
                        yield true;
                    }
                    case R.id.menu_account -> {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentAccount()).commit();
                        yield true;
                    }
                    default -> false;
                };
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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

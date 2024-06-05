package com.tester.iotss.Services;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tester.iotss.R;

public class MyBackgroundService extends Service {

    private static final int SERVICE_NOTIFICATION_ID = 123;

    @SuppressLint("ForegroundServiceType")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(SERVICE_NOTIFICATION_ID, createNotification());
        // Jalankan operasi Anda di sini
        // Misalnya, operasi Firebase Messaging di latar belakang
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification createNotification() {
        String channelId = "MyBackgroundServiceChannel";
        String channelName = "My Background Service Channel";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Background Service")
                .setContentText("Service is running in the background")
                .setSmallIcon(R.drawable.logohitam);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        return builder.build();
    }
}

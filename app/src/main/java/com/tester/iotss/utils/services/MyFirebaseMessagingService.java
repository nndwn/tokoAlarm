package com.tester.iotss.utils.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tester.iotss.utils.sessions.SessionLogin;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingServic";
    SessionLogin sessionLogin = new SessionLogin();
    private ScheduleService scheduleService;
    private boolean isBound = false;
    public MyFirebaseMessagingService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        // Subscribe to a topic (if needed)
        FirebaseMessaging.getInstance().subscribeToTopic(sessionLogin.getNohp(this))
                .addOnCompleteListener(task -> {
                    String msg = "Subscribed to topic";
                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }
                    Log.d(TAG, msg);
                });
        Intent intent = new Intent(this, ScheduleService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle(); //get title
        String message = remoteMessage.getNotification().getBody(); //get message
        String tag = remoteMessage.getNotification().getTag();

        if (isBound && scheduleService != null) {
            scheduleService.createNotification(false, title, message, "your_channel_id");
        } else {
            Log.d(TAG, "Service not bound");
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        sendRegistrationToServer(token);
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendRegistrationToServer(String token) {
        // Implement this method to send token to your app server
        // Implementasikan metode ini untuk mengirim token ke server aplikasi Anda
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ScheduleService.LocalBinder binder = (ScheduleService.LocalBinder) service;
            scheduleService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

}

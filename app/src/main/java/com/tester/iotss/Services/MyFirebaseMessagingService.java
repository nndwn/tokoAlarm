package com.tester.iotss.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tester.iotss.Home;
import com.tester.iotss.R;
import com.tester.iotss.Session.SessionLogin;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingServic";
    SessionLogin sessionLogin = new SessionLogin();

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle(); //get title
        String message = remoteMessage.getNotification().getBody(); //get message
        String tag = remoteMessage.getNotification().getTag();

        Log.d(TAG, "Message Notification Title: " + title);
        Log.d(TAG, "Message Notification Body: " + message);
        Log.d(TAG, "Message Notification Nomor HP: " + tag);
        Log.d(TAG, "Message Notification Session Login Nomor HP: " + sessionLogin.getNohp(this));

        sendNotification(title, message, tag);
    }

    @Override
    public void onNewToken(String token) {
        sendRegistrationToServer(token);
    }

    @Override
    public void onDeletedMessages() {

    }

    private void sendRegistrationToServer(String token) {
        // Implement this method to send token to your app server
        // Implementasikan metode ini untuk mengirim token ke server aplikasi Anda
    }

    private void sendNotification(String title, String message, String tag) {
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        SessionLogin sessionLogin = new SessionLogin();
        Uri defaultSoundUri = sessionLogin.getUrialarm(getApplicationContext());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "TOKOALARM220";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            NotificationChannel mChannel = new NotificationChannel(channelId,
                    getApplicationContext().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            mChannel.setDescription("Aplikasi Tokoalarm sukses menampilkan notifikasi");
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setSound(defaultSoundUri, attributes); // This is IMPORTANT
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mChannel.setShowBadge(true);
            mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logohitam)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setChannelId(channelId)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(message))
                    .setFullScreenIntent(pendingIntent, true);
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
            notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            notificationBuilder.setLights(Color.RED, 3000, 3000);
            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(1, notificationBuilder.build());
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logohitam)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setChannelId(channelId)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationBuilder.setPriority(Notification.PRIORITY_MAX);
            notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            notificationBuilder.setLights(Color.RED, 3000, 3000);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    public static String generateRandomString(int length) {
        // Karakter yang mungkin digunakan dalam string acak
        String characters = "0123456789";
        StringBuilder stringBuilder = new StringBuilder();

        // Pembangkit bilangan acak
        Random random = new Random();

        // Membuat string acak dengan panjang yang ditentukan
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}

package com.tester.iotss.utils.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tester.iotss.R;
import com.tester.iotss.data.AppConstant;
import com.tester.iotss.data.remote.api.ApiService;
import com.tester.iotss.data.remote.network.RetrofitClient;
import com.tester.iotss.data.remote.request.GetUserScheduleRequest;
import com.tester.iotss.data.remote.response.ScheduleResponse;
import com.tester.iotss.domain.model.Schedule;
import com.tester.iotss.ui.activity.Home;
import com.tester.iotss.utils.Utils;
import com.tester.iotss.utils.helpers.MqttHelper;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleService extends Service {
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "STOP_FOREGROUND_SERVICE";
    private static final String TAG = "ScheduleService";
    private List<Schedule> scheduleList ;
    private final Handler handler = new Handler();
    private Timer timer;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", new Locale("id", "ID"));

    private final ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
    SessionLogin sessionLogin = new SessionLogin();
    private static final String BrokerUri = AppConstant.MQTT_SERVER_PROTOCOL + AppConstant.MQTT_SERVER_HOST + ":" + AppConstant.MQTT_SERVER_PORT;
    private MqttHelper mqttHelper;

    @Override
    public void onCreate(){
        super.onCreate();
        scheduleList = new ArrayList<>();
        timer = new Timer();
        startForegroundService();
        fetchSchedules();
        startService();

    }

    private void startForegroundService() {
        String channelId = "SchedulerServiceChannel";
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    channelId,
                    "Scheduler Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
        }

        NotificationManager manager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager = getSystemService(NotificationManager.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Mode Jadwal Aktif")
                .setContentText("Service is running in the background")
                .setSmallIcon(R.drawable.logo_icon)
                .build();

        startForeground(1, notification);
    }


    private void createNotification(String name, String message) {
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound_notification_1);
        String channelId = "TOKOALARM220";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId,
                    getApplicationContext().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            mChannel.setDescription("Notifikasi Toko Alarm");
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setSound(defaultSoundUri, new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build());
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(mChannel);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_icon)
                    .setContentTitle("Jadwal Alarm " + name)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX);
            notificationManager.notify(1, notificationBuilder.build());
        }else
        {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_icon)
                    .setContentTitle("Jadwal Alarm " + name)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            notificationManager.notify(1, notificationBuilder.build());
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {

        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_STOP_FOREGROUND_SERVICE.equals(action)) {
                stopForegroundService();
                Log.d("Schedule", "STOP SERVICE");
            }
        }
        return  START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startService(){
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        checkSchedules();
                    }
                });};
        },0, 1000);
    }

    private void fetchSchedules(){
        GetUserScheduleRequest requestBody = new GetUserScheduleRequest(sessionLogin.getNohp(getApplicationContext()));
        Call<ScheduleResponse> call = apiService.getUserSchedules(requestBody);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ScheduleResponse> call, @NonNull Response<ScheduleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    scheduleList.clear();
                    scheduleList.addAll(response.body().getData());
                    Log.d(TAG, "Schedules fetched successfully");
                } else {
                    scheduleList.clear();
                    Log.d(TAG, "Failed to fetch schedules");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScheduleResponse> call, @NonNull Throwable throwable) {
                Log.e(TAG, Objects.requireNonNull(throwable.getMessage()));

            }
        });
    }

    public void startMqtt( int i, String id_alat, String value) {
        disconnectMqtt();
        mqttHelper = new MqttHelper(this, BrokerUri, AppConstant.MQTT_USER,AppConstant.MQTT_PASSWORD);
        mqttHelper.connect(sessionLogin.getNohp(getApplicationContext()), sessionLogin.getPassword(getApplicationContext()));
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                if (Objects.equals(scheduleList.get(i).getIs_active(), "1"))
                {
                    sendToServer(scheduleList.get(i).getSensor_switch(),id_alat+"/statin1",value);
                    sendToServer(scheduleList.get(i).getSensor_ohm(),id_alat+"/statin2",value);
                    sendToServer(scheduleList.get(i).getSensor_rf(),id_alat+"/statin3",value);
                }

                Log.d("abah", "SERVER MQTT KONEK");
            }

            @Override
            public void connectionLost(Throwable throwable) {}
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {}
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}
        });
    }

    private void sendToServer( String sensor, String topic, String datanya) {

        if (Objects.equals(sensor, "1")) {
            if (mqttHelper.mqttAndroidClient.isConnected()) {
                MqttMessage message = new MqttMessage();
                message.setPayload(datanya.getBytes());
                message.setQos(0);
                message.setRetained(false);
                mqttHelper.mqttAndroidClient.publish(topic, message);
            } else {
                Log.d("Publish", "Enggak Bisa publish");
            }
        }
    }

    private void disconnectMqtt() {
        if (mqttHelper != null) {
            if (mqttHelper.mqttAndroidClient != null) {
                if (mqttHelper.mqttAndroidClient.isConnected()) {
                    Log.d("FragmentHomeLog", "berhasil diskonek mqtt");
                    try {
                        mqttHelper.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("FragmentHomeLog", "gagal diskonek mqtt");
                    }
                }
                mqttHelper = null;
            }
        }
    }

    private void checkSchedules() {
        if (scheduleList == null) return;
        String day= "";
        boolean[] end = new boolean[scheduleList.size()];
        Date currentTime = new Date();
        LocalDate today ;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDate.now();
            day = today.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("id", "ID"));
        }
        String formattedTime = dateFormat.format(currentTime);
        for (int i = 0; i <  scheduleList.size(); i++) {
            String[] numbersArray = scheduleList.get(i).getDays().split(",");
            for (String s : numbersArray) {
                if (s.equalsIgnoreCase(Utils.getDayNumber(day))) {
                    Log.d(TAG, String.valueOf(!end[i]));
                    if (!end[i] && formattedTime.equals(scheduleList.get(i).getStart_time()))  {
                        Log.d(TAG, "Sudah mulai" );
                        createNotification(scheduleList.get(i).getName(), "Memulai Jadwal Alarm");
                        startMqtt(i, scheduleList.get(i).getId_alat(), "1");
                        end[i] = true;
                    }
                }
            }
            if ( end[i]  && formattedTime.equals(scheduleList.get(i).getEnd_time())) {
                Log.d(TAG, "Sudah berakhir");
                createNotification(scheduleList.get(i).getName(), "Jadwal Alarm Berakhir");
                startMqtt(i, scheduleList.get(i).getId_alat(), "0");
                end[i] = false;
            }

        }
    }

    public void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        disconnectMqtt();
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }
}

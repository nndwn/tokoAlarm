package com.tester.iotss.utils.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tester.iotss.data.AppConstant;
import com.tester.iotss.utils.helpers.MqttHelper;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttService extends Service {
    private static MqttCallbackListener callbackListener;
    public interface MqttCallbackListener {
        void onMqttConnected(boolean reconnect, String serverURI);
        void onMqttConnectionLost(Throwable throwable);
        void onMqttMessageReceived(String topic, String message);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MqttHelper mqttHelper = new MqttHelper(this, AppConstant.MQTT_SERVER_PROTOCOL + AppConstant.MQTT_SERVER_HOST + ":" + AppConstant.MQTT_SERVER_PORT, AppConstant.MQTT_USER, AppConstant.MQTT_PASSWORD);
        SessionLogin sessionLogin = new SessionLogin();
        mqttHelper.connect(sessionLogin.getNohp(getApplicationContext()), sessionLogin.getPassword(getApplicationContext()));


        // Set the callback listener
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if (callbackListener != null) {
                    callbackListener.onMqttConnected(reconnect, serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                if (callbackListener != null) {
                    callbackListener.onMqttConnectionLost(throwable);
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Handle incoming MQTT messages
                if(callbackListener != null){
                    callbackListener.onMqttMessageReceived(topic,message.toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // Rest of your MqttService code
}


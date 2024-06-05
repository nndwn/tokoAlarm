package com.tester.iotss.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tester.iotss.MqttHelper;
import com.tester.iotss.Session.SessionLogin;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttService extends Service {
    private MqttHelper mqttHelper;
    private static MqttCallbackListener callbackListener;
    public interface MqttCallbackListener {
        void onMqttConnected(boolean reconnect, String serverURI);
        void onMqttConnectionLost(Throwable throwable);
        void onMqttMessageReceived(String topic, String message);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Initialize and connect your MQTT client here
        mqttHelper = new MqttHelper(this, "tcp://149.28.133.74:1884");
        SessionLogin sessionLogin = new SessionLogin();
        mqttHelper.connect(sessionLogin.getNohp(getApplicationContext()), sessionLogin.getPassword(getApplicationContext()));

        // Set the callback listener
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                // MQTT connection is established or re-established
                if (callbackListener != null) {
                    callbackListener.onMqttConnected(reconnect, serverURI);
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                // MQTT connection is lost
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
                // Message delivery is complete
            }
        });

        // Ensure that the service keeps running, even if the app is in the background
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // Rest of your MqttService code
}


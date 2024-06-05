package com.tester.iotss;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;


public class MqttHelper {
    public MqttAndroidClient mqttAndroidClient;


    String BrokerUri;
    public MqttHelper(Context context,String BrokerUri){
        this.BrokerUri=BrokerUri;
        try {
            mqttAndroidClient = new MqttAndroidClient(context, BrokerUri, UUID.randomUUID().toString(), Ack.AUTO_ACK);
        }catch (Exception ex) {
            Log.w("abah", "MqttException:"+ex.toString());
        }
    }


    public void connect(String mqtt_username,String mqtt_password){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setUserName(mqtt_username);
        mqttConnectOptions.setPassword(mqtt_password.toCharArray());
        mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

                DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                disconnectedBufferOptions.setBufferEnabled(true);
                disconnectedBufferOptions.setBufferSize(4096);
                disconnectedBufferOptions.setPersistBuffer(false);
                disconnectedBufferOptions.setDeleteOldestMessages(true);
                mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.d("abah", "Mqtt failed connect to: " + BrokerUri + exception.toString());
            }
        });
    }


    public void disconnect()
            throws MqttException {
        IMqttToken mqttToken = mqttAndroidClient.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Log.d(TAG, "Successfully disconnected");
            }
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Log.d(TAG, "Failed to disconnected " + throwable.toString());
            }
        });
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }

}

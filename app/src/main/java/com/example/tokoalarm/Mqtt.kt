package com.example.tokoalarm

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import java.util.UUID

class Mqtt {
    private lateinit var mqttClient: Mqtt3AsyncClient

    fun connect(callback :(Boolean) ->Unit = {} ) {
        mqttClient = Mqtt3Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(MQTT_SERVER_HOST)
            .serverPort(MQTT_SERVER_PORT)
            .buildAsync()

        mqttClient.connectWith()
            .simpleAuth()
            .username(MQTT_USER)
            .password(MQTT_PASSWORD.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { _, throwable ->
                callback (throwable == null)
            }
    }

    fun disconnect(callback: () -> Unit = {}){
        if (this::mqttClient.isInitialized) {
            mqttClient.disconnect()
        }
        callback()
    }

    fun subscribe (topic : String , callback: (String) -> Unit ={}) {
        mqttClient.subscribeWith()
            .topicFilter(topic)
            .callback { publish ->
                callback(String(publish.payloadAsBytes))
            }
            .send()
    }

    fun unSubscribe (topic: String , callback: () -> Unit = {}) {
        mqttClient.unsubscribeWith()
            .topicFilter(topic)
            .send()
            .whenComplete { _, throwable ->
                if (throwable == null) {
                    callback()
                }
            }
    }

    fun publish (topic :String, message :String , callback : (Boolean) -> Unit = {}) {
        mqttClient.publishWith()
            .topic(topic)
            .payload(message.toByteArray())
            .send()
            .whenComplete { _, throwable ->
                callback(throwable == null)
            }
    }

}
package com.example.tokoalarm

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
//todo:pada server perrlu di upgrade versi mqtt
class HandlerMqtt {
    private lateinit var mqttClient: Mqtt3AsyncClient

    fun connect(callback: (boolean: Boolean) -> Unit = {}) {
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
                if (throwable != null) {
                    callback(false)
                    println("Connection failed: ${throwable.message}")
                } else {
                    callback(true)
                    println("Connected to MQTT broker")
                }
            }
    }

    fun subscribe (topic :String, callback: (message: String) -> Unit = {}) {
        mqttClient.subscribeWith()
            .topicFilter(topic)
            .callback { publish ->
                val message = String(publish.payloadAsBytes)
                callback(message)
            }
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    println("Subscription failed: ${throwable.message}")
                    callback("")
                } else {
                    println("Subscribed successfully")
                }
            }
    }

    fun publish (topic :String, message :String, callback: (boolean: Boolean) -> Unit = {}) {
        mqttClient.publishWith()
            .topic(topic)
            .payload(message.toByteArray())
            .send()
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    callback(false)
                    println("Publishing failed: ${throwable.message}")
                } else {
                    callback(true)
                    println("Published message: $message")

                }
            }
    }

    fun disconnect() {
        mqttClient.disconnect()
            .whenComplete {_ , throwable ->
                if (throwable != null) {
                    println("Disconnection failed: ${throwable.message}")
                } else {
                    println("Disconnected from MQTT broker")
                }
            }
    }
}
package com.example.tokoalarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
//todo:pada server perrlu di upgrade versi mqtt

class ViewModelMonitor : ViewModel() {
    private val _connectionStatus = MutableLiveData<Boolean>()
    val connectionStatus: LiveData<Boolean> get() = _connectionStatus

    private val _alatConnectionStatus = MutableLiveData<Boolean>()
    val alatConnectionStatus: LiveData<Boolean> get() = _alatConnectionStatus


    val jadwal :MutableLiveData<ListJadwal?> = MutableLiveData()
    val alat : MutableLiveData<ListAlat> = MutableLiveData()
    val mode :MutableLiveData<String> = MutableLiveData()
    val delay : MutableLiveData<Long> = MutableLiveData()
    val sensor : MutableLiveData<List<ListSensor>> = MutableLiveData()


    private lateinit var mqttClient: Mqtt3AsyncClient
    private var isConnected = false
    private var lastMessageTime: Long = 0
    private val connectionTimeout = 10000L

    private val scope = CoroutineScope(Dispatchers.Main)


    fun getData (phone : String , idAlat : String, idPaket : String) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.getDetailSettingAlat(
                phone,
                idAlat,
                idPaket
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    mode.value = it.lastAlat.data.mode
                    delay.value = it.lastAlat.data.delay.toLong()
                    sensor.value = toListSensor(it.lastAlat.data)
                }

            }
        }
    }

    private fun toListSensor(listSetting: ListSetting) :List<ListSensor> {
        return listOf(
            ListSensor(
                name = "Sensor Switch",
                inn = listSetting.in1,
                outt = listSetting.out1,
                stsin = listSetting.stsin1,
                statin = listSetting.statin1,
                stsstatin = listSetting.stsstatin1
            ),

            ListSensor(
                name = "Sensor Temperature (Ohm)",
                inn = listSetting.in2,
                outt = listSetting.out2,
                stsin = listSetting.stsin2,
                statin = listSetting.statin2,
                stsstatin = listSetting.stsstatin2
            ),

            ListSensor(
                name = "Sensor Gerak (RF)",
                inn = listSetting.in3,
                outt = listSetting.out3,
                stsin = listSetting.stsin3,
                statin = listSetting.statin3,
                stsstatin = listSetting.stsstatin3
            )
        )
    }

    fun publish (topic :String, message :String) {
        mqttClient.publishWith()
            .topic(topic)
            .payload(message.toByteArray())
            .send()
            .whenComplete { _, throwable ->
                isConnected = throwable == null
                _connectionStatus.postValue(isConnected)
            }
    }


    fun subsAlat (topic :String,) {
        mqttClient.subscribeWith()
            .topicFilter(topic)
            .callback { publish ->
                val message = String(publish.payloadAsBytes)
                lastMessageTime = System.currentTimeMillis()
                scope.launch {
                    if (message == "1") {
                        _alatConnectionStatus.postValue(true)
                    }
                }
            }
            .send()
        startConnectionMonitor()
    }

    private fun startConnectionMonitor() {
        scope.launch {
            while (true) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastMessageTime > connectionTimeout) {
                    _alatConnectionStatus.postValue(false)
                }
                delay(2000)
            }
        }
    }

    fun connect() {
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
                isConnected = throwable == null
                _connectionStatus.postValue(isConnected)
                if (isConnected) {
                    startConnectionCheckLoop()
                } else {
                    startReconnectLoop()
                }
            }
    }

    private fun startReconnectLoop() {
        scope.launch {
            while (!isConnected) {
                delay(2000)
                connect()
            }
        }
    }

    private fun startConnectionCheckLoop() {
        scope.launch {
            while (isConnected) {
                delay(2000)
                publish("check", "0")
                if ( !isConnected) {
                    startReconnectLoop()
                }
            }
        }
    }


    fun disconnect() {
        scope.cancel()
        mqttClient.disconnect()
        isConnected = false
        _connectionStatus.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
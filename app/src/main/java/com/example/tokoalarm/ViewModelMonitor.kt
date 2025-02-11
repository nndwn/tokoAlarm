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
    private var lastMessageTime: Long = 0
    private val connectionTimeout = 10000L

    private val _sensorSwicthStatus = MutableLiveData<Boolean>()
    val sensorSwicthStatus: LiveData<Boolean> get() = _sensorSwicthStatus
    private var lastSensorSwitch :Long = 0

    private val _sensorTemperatureStatus = MutableLiveData<Boolean>()
    private val sensorTemperatureStatus: LiveData<Boolean> get() = _sensorTemperatureStatus
    private var lastSensorTemperature :Long = 0

    private val _sensorGerakStatus = MutableLiveData<Boolean>()
    val sensorGerakStatus: LiveData<Boolean> get() = _sensorGerakStatus
    private var lastSensorGerak :Long = 0


    val jadwal :MutableLiveData<ListJadwal?> = MutableLiveData()
    val alat : MutableLiveData<ListAlat> = MutableLiveData()
    val mode :MutableLiveData<String> = MutableLiveData()
    val delay : MutableLiveData<Long> = MutableLiveData()
    val sensor : MutableLiveData<List<ListSensor>> = MutableLiveData()


    private lateinit var mqttClient: Mqtt3AsyncClient
    private var isConnected = false


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
                    sensor.value = toListSensor(it.lastAlat.data, it.renamed.data)
                }
            }
        }
    }

    fun renameSensor(phone: String, idAlat: String, type : String, rename : String) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.gerRenameNameSensor(
                phone,
                idAlat,
                type,
                rename
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    println("success")
                }
            }
        }
    }


    private fun convertListRenamedData(listName: List<ListRenamedAlat>, str: String): String {
        val result = listName.find { it.beforeRenamed == str }
        return result?.afterRenamed ?: ""
    }

    private fun toListSensor(listSetting: ListSetting , rename : List<ListRenamedAlat>) :List<ListSensor> {
        return listOf(
            ListSensor(
                check = false,
                name = "Sensor Switch",
                inn = listSetting.in1.toString(),
                outt = listSetting.out1.toString(),
                stsin = listSetting.stsin1.toString(),
                statin = listSetting.statin1.toString(),
                stsstatin = listSetting.stsstatin1.toString(),
                rename = convertListRenamedData(rename, "in1"),
                type = "/in1",
                typeStatin = "/statin1"

            ),

            ListSensor(
                check = false,
                name = "Sensor Temperature (Ohm)",
                inn = listSetting.in2.toString(),
                outt = listSetting.out2.toString(),
                stsin = listSetting.stsin2.toString(),
                statin = listSetting.statin2.toString(),
                stsstatin = listSetting.stsstatin2.toString(),
                rename = convertListRenamedData(rename, "in2"),
                type = "/in2",
                typeStatin = "/statin2"

            ),

            ListSensor(
                check = false,
                name = "Sensor Gerak (RF)",
                inn = listSetting.in3 .toString(),
                outt = listSetting.out3.toString(),
                stsin = listSetting.stsin3.toString(),
                statin = listSetting.statin3.toString(),
                stsstatin = listSetting.stsstatin3.toString(),
                rename = convertListRenamedData(rename, "in3"),
                type = "/in3",
                typeStatin = "/statin3"
            )
        )
    }

    fun publish (topic :String, message :String, callback : () -> Unit = {}) {
        mqttClient.publishWith()
            .topic(topic)
            .payload(message.toByteArray())
            .send()
            .whenComplete { _, throwable ->
                isConnected = throwable == null
                _connectionStatus.postValue(isConnected)
                if(isConnected) {
                    callback()
                }
            }
    }


    fun unSubsAlat (topic: String) {
        mqttClient.unsubscribeWith()
            .topicFilter(topic)
            .send()
    }

    fun subsAlat (topic :String, type : String ) {
        mqttClient.subscribeWith()
            .topicFilter(topic)
            .callback { publish ->
                val message = String(publish.payloadAsBytes)
                when (type) {
                    "alat" -> lastMessageTime = System.currentTimeMillis()
                    "/in1" -> lastSensorSwitch = System.currentTimeMillis()
                    "/in2" -> lastSensorTemperature = System.currentTimeMillis()
                    "/in3" -> lastSensorGerak = System.currentTimeMillis()
                }

                scope.launch {
                    if (message == "1") {
                        when (type) {
                            "alat" ->  _alatConnectionStatus.postValue(true)
                            "/in1" -> _sensorSwicthStatus.postValue(true)
                            "/in2" -> _sensorTemperatureStatus.postValue(true)
                            "/in3" -> _sensorGerakStatus.postValue(true)
                        }

                    }
                }
            }
            .send()
        startConnectionMonitor(type)
    }

    private fun startConnectionMonitor(type: String) {
        scope.launch {
            while (true) {
                val currentTime = System.currentTimeMillis()
                when (type) {
                    "alat" -> {
                        if (currentTime - lastMessageTime > connectionTimeout) {
                            _alatConnectionStatus.postValue(false)
                        }
                    }
                    "/in1" -> {
                        if (currentTime - lastSensorSwitch > connectionTimeout) {
                            _sensorSwicthStatus.postValue(false)
                        }
                    }
                    "/in2" -> {
                        if (currentTime - lastSensorTemperature > connectionTimeout) {
                            _sensorTemperatureStatus.postValue(false)
                        }
                    }
                    "/in3" -> {
                        if (currentTime - lastSensorGerak > connectionTimeout) {
                            _sensorGerakStatus.postValue(false)
                        }
                    }
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


    private fun disconnect() {
        scope.cancel()
        if (this::mqttClient.isInitialized){
            mqttClient.disconnect()
        }
        isConnected = false
        _connectionStatus.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}
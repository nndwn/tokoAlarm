package com.example.tokoalarm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//todo:pada server perrlu di upgrade versi mqtt

class ViewModelMonitor : ViewModel() {


    val jadwal :MutableLiveData<ListJadwal?> = MutableLiveData()
    val alat : MutableLiveData<ListAlat> = MutableLiveData()

    val lamaBunyi : MutableLiveData<Long> = MutableLiveData()

    private val _sensor = MutableLiveData<List<ListSensor>>()
    val sensor : LiveData<List<ListSensor>> get() = _sensor

    private val mqtt = Mqtt()
    private val _connectionStatus = MutableLiveData<Boolean>()
    val connectionStatus: LiveData<Boolean> get() = _connectionStatus

    private val _refresh = MutableLiveData<Boolean>()
    val refresh : LiveData<Boolean> get() = _refresh
    private val _checkAlat  = MutableLiveData<Boolean>()
    val checkAlat : LiveData<Boolean> get() = _checkAlat
    val btnActive :MutableLiveData<Boolean> = MutableLiveData(false)
    private val mode :MutableLiveData<String> = MutableLiveData()

    fun connectMqtt(){
        _refresh.postValue(true)
        mqtt.connect {
            viewModelScope.launch {
                _connectionStatus.postValue(it)
                if (it) {
                    _refresh.postValue(false)
                    changeManualtoAuto()
                    connectAlat()
                }
            }
        }
    }

    fun publishSettingsAlat (str :String, post: String) {
        mqtt.connect {
            if (it) {
                mqtt.publish(str, post) {
                    mqtt.disconnect()
                }
            }
        }
    }

    private suspend fun connectAlat () {
        mqtt.publish(alat.value!!.idAlat + "/active", "0") {
            mqtt.subscribe(alat.value!!.idAlat + "/active") { msg ->
                if (msg == "1") {
                    _checkAlat.postValue(true)
                } else if (msg == "0") {
                    _checkAlat.postValue(false)
                }
            }
        }
        delay(10000)
        mqtt.unSubscribe(alat.value!!.idAlat + "/active") {
            mqtt.disconnect()
        }
        btnActive.value = true
    }

    private fun changeManualtoAuto () {
        if (mode.value == "manual") {
            viewModelScope.launch {
                mqtt.publish(alat.value!!.idAlat + "/mode", "otomatis")
            }
        }
    }


    fun getData (phone : String , idAlat : String, idPaket : String) {
        viewModelScope.launch {
            _refresh.postValue(true)
            val response = RetrofitClient.apiService.getDetailSettingAlat(
                phone,
                idAlat,
                idPaket
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    mode.value = it.lastAlat.data.mode
                    lamaBunyi.value = it.lastAlat.data.delay.toLong()
                    _sensor.postValue(toListSensor(it.lastAlat.data, it.renamed.data))
                    _refresh.postValue(false)
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

}
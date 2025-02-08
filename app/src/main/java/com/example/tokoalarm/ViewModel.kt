package com.example.tokoalarm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class SharedViewMainActivity : ViewModel() {
    val saldo : MutableLiveData<String> = MutableLiveData()
    val linkPemesanan : MutableLiveData<String> = MutableLiveData()
    val listAlat : MutableLiveData<List<ListAlat>> = MutableLiveData()
    val listJadwal : MutableLiveData<List<ListJadwal>> = MutableLiveData()

    fun getJadwal (phone : String , callback: (str: String) -> Unit = {}) {
        viewModelScope.launch {
            val jsonBody = JsonObject().apply {
                addProperty("no_hp", phone)
            }
            val response = RetrofitClient.apiService.getListJadwal(API_KEY, jsonBody)
            if (!response.isSuccessful) {
                callback("connection")
                return@launch
            }
            val body = response.body()
            if (body?.status != 200) {
                callback("failed")
                return@launch
            }
            listJadwal.value = body.data
            callback( "success")
        }
    }

}

class SharedViewTopUp : ViewModel() {
    val price : MutableLiveData<Int> = MutableLiveData()
    val methodPayment : MutableLiveData<BankAccount> = MutableLiveData()
}

class SharedViewAddJadwal : ViewModel() {
    val listAddJadwal : MutableLiveData<List<ListAddJadwal>> = MutableLiveData()
    fun show(phone: String) {
        viewModelScope.launch {
            val jsonBody = JsonObject().apply {
                addProperty("no_hp", phone)
            }
            val response = RetrofitClient.apiService.getListAddJadwal(API_KEY, jsonBody)
            if (!response.isSuccessful) {
                return@launch
            }
            val body = response.body()
            if (body?.status != 200) {
                return@launch
            }
            listAddJadwal.value = body.data
        }
    }
}

class SharedViewPilihPaket : ViewModel() {
    val paket : MutableLiveData<List<ListPaket>> = MutableLiveData()
    val position : MutableLiveData<Int> = MutableLiveData()
    val idAlat : MutableLiveData<String?> = MutableLiveData()
    val saldo : MutableLiveData<Int?> = MutableLiveData()
    fun input(phone :String , listPaket: ListPaket , idUsers: String , nomorAlat : String ,callback :( str :String) -> Unit ={}) {
        viewModelScope.launch {
            val response = RetrofitClient.apiService.beliPaket(
                phone,
                listPaket.periode,
                listPaket.dayConvertion,
                listPaket.cutoffDay,
                listPaket.biaya,
                idUsers,
                nomorAlat
            )
            if (!response.isSuccessful) {
                callback("connection")
                return@launch
            }
            val body = response.body()
            if (body?.status != true) {
                callback("failed")
                return@launch
            }
            callback( "success")

        }
    }
}

class SharedViewMonitoring :ViewModel() {
    val jadwal :MutableLiveData<ListJadwal?> = MutableLiveData()
    val alat : MutableLiveData<ListAlat> = MutableLiveData()
    val mode :MutableLiveData<String> = MutableLiveData()
}
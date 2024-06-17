package com.tester.iotss.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.tester.iotss.data.local.database.AppDatabase
import com.tester.iotss.data.model.Perangkat
import com.tester.iotss.data.remote.api.ApiService
import com.tester.iotss.data.remote.network.RetrofitClient
import com.tester.iotss.data.repository.PerangkatRepository
import kotlinx.coroutines.launch

class PerangkatViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PerangkatRepository

    val allPerangkat: LiveData<List<Perangkat>>

    init {
        val apiService = RetrofitClient.createService(ApiService::class.java)
        val db = AppDatabase.getDatabase(application)
        repository = PerangkatRepository(apiService, db.perangkatDao())
        allPerangkat = repository.allPerangkat
    }

    fun refreshPerangkat(noHp: String) {
        viewModelScope.launch {
            repository.refreshPerangkat(noHp)
        }
    }
}

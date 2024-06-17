package com.tester.iotss.data.repository

import androidx.lifecycle.LiveData
import com.tester.iotss.data.local.database.PerangkatDao
import com.tester.iotss.data.model.Perangkat
import com.tester.iotss.data.remote.api.ApiService
import com.tester.iotss.data.remote.request.PerangkatRequest

class PerangkatRepository(private val apiService: ApiService, private val perangkatDao: PerangkatDao) {

    val allPerangkat: LiveData<List<Perangkat>> = perangkatDao.getAllPerangkat()

    suspend fun refreshPerangkat(noHp: String) {
        try {
            val response = apiService.fetchPerangkat(PerangkatRequest(noHp))
            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    perangkatDao.insertAll(apiResponse.data)
                }
            } else {
                // Handle error
            }
        } catch (e: Exception) {
            // Handle exception
        }
    }
}

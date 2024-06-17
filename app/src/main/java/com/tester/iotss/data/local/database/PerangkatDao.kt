package com.tester.iotss.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tester.iotss.data.model.Perangkat

@Dao
interface PerangkatDao {
    @Query("SELECT * FROM perangkat")
    fun getAllPerangkat(): LiveData<List<Perangkat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(perangkats: List<Perangkat>)
}
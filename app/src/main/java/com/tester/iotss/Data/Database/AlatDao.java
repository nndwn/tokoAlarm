package com.tester.iotss.Data.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tester.iotss.Data.Model.Alat;

import java.util.List;
@Dao
public interface AlatDao {

    @Query("SELECT * FROM alat")
    LiveData<List<Alat>> getAllAlat();

    @Insert
    void insertAll(List<Alat> alats);
}

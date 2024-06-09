package com.tester.iotss.Data.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tester.iotss.Data.Model.Alat;

@Database(entities = {Alat.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlatDao alatDao();
}
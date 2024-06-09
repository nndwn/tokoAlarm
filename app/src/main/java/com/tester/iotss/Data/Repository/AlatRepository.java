package com.tester.iotss.Data.Repository;
import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.tester.iotss.Data.Database.AlatDao;
import com.tester.iotss.Data.Database.AppDatabase;
import com.tester.iotss.Data.Model.Alat;

import java.util.List;
public class AlatRepository {
    private AlatDao alatDao;
    private LiveData<List<Alat>> allAlatItems;

    public AlatRepository(Context context) {
        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "item-database").build();
        alatDao = db.alatDao();
        allAlatItems = alatDao.getAllAlat();
    }

    public LiveData<List<Alat>> getAllAlatItems() {
        return allAlatItems;
    }

    public void insertAll(List<Alat> items) {
        new Thread(() -> alatDao.insertAll(items)).start();
    }
}

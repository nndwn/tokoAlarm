package com.tester.iotss.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tester.iotss.data.model.Perangkat
import com.tester.iotss.utils.SensorListConverter

@Database(entities = [Perangkat::class], version = 1, exportSchema = false)
@TypeConverters(SensorListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun perangkatDao(): PerangkatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "toko_alarm"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

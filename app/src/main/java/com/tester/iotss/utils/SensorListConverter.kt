package com.tester.iotss.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tester.iotss.domain.model.Sensor

class SensorListConverter {

    @TypeConverter
    fun fromSensorList(sensorList: List<Sensor>?): String? {
        if (sensorList == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Sensor>>() {}.type
        return gson.toJson(sensorList, type)
    }

    @TypeConverter
    fun toSensorList(sensorListString: String?): List<Sensor>? {
        if (sensorListString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<Sensor>>() {}.type
        return gson.fromJson(sensorListString, type)
    }
}

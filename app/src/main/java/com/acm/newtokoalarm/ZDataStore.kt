package com.acm.newtokoalarm

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "preferences")

class ZDataStore(private val context: Context) {
    private val  appDataKey = stringPreferencesKey("appdata")
    private val  promoBannerKey = stringSetPreferencesKey("promoBanner")
    private val  authenKey = stringPreferencesKey("aunthenfication")

    val appDataFlow : Flow<String?> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException){
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map {
            it[appDataKey]
        }

    val promoBannerFlow : Flow<Set<String>> = context.dataStore.data
        .catch {
            if (it is IOException){
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[promoBannerKey] ?: emptySet()
        }

    val authenFlow : Flow<String?> = context.dataStore.data
        .catch {
            if (it is IOException){
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map {
            it[authenKey]
        }

    suspend fun setAuthen(data: String) {
        context.dataStore.edit {
            it[authenKey] = data
        }
    }
    suspend fun setAppData(data : String) {
        context.dataStore.edit {
            it[appDataKey] = data
        }
    }
    suspend fun setPromoBanner(banner: Set<String>) {
        context.dataStore.edit {
            val currentPaths = it[promoBannerKey] ?: emptySet()
            val updatePaths = currentPaths + banner
            it[promoBannerKey] = updatePaths
        }
    }

}
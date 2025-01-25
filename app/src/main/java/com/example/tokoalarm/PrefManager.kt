package com.example.tokoalarm

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

class PrefManager (private val context: Context){
    private val idAlatKey = stringPreferencesKey("id_alat")
    private val pwdKey = stringPreferencesKey("password")
    private val idUserKey = stringPreferencesKey("id_users")
    private val nameUserKey = stringPreferencesKey("name_user")
    private val phoneKey = stringPreferencesKey("phone_number")
    private val imagePathsKey = stringSetPreferencesKey("image_paths")

    val idAlatFlow: Flow<String?> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { pref ->
            pref[idAlatKey]
        }
    val pwdFlow: Flow<String?> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { pref ->
            pref[pwdKey]
        }
    val idUserFlow :Flow<String?> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { pref ->
            pref[idUserKey]
        }
    val nameUserFlow :Flow<String?> = context.dataStore.data
        .catch { ex -> if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { pref ->
            pref[nameUserKey]
        }
    val phoneFlow :Flow <String?> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { pref ->
            pref[phoneKey]
        }

    val imagePathsFlow: Flow<Set<String>> = context.dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { pref ->
            pref[imagePathsKey] ?: emptySet()
        }

    suspend fun setIdAlat(idAlat: String) {
        context.dataStore.edit { pref ->
            pref[idAlatKey] = idAlat
        }
    }
    suspend fun setPwd(pwd: String) {
        context.dataStore.edit { pref ->
            pref[pwdKey] = pwd
        }
    }
    suspend fun setIdUser (idUser: String) {
        context.dataStore.edit { pref ->
            pref[idUserKey] = idUser
        }
    }
    suspend fun setNameUser(nameUser: String) {
        context.dataStore.edit { pref ->
            pref[nameUserKey] = nameUser
        }
    }
    suspend fun setPhone(phone : String) {
        context.dataStore.edit { pref ->
            pref[phoneKey] = phone
        }
    }
    suspend fun setImagePaths(imagePaths: Set<String>) {
        context.dataStore.edit { pref ->
            val currentPaths = pref[imagePathsKey] ?: emptySet()
            val updatedPaths = currentPaths + imagePaths
            pref[imagePathsKey] = updatedPaths
        }
    }
}
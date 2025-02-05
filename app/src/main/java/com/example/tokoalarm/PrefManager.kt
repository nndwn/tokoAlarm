package com.example.tokoalarm

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "preferences")

class PrefManager(private val context: Context) {
    private val idAlatKey = stringPreferencesKey("id_alat")
    private val pwdKey = stringPreferencesKey("password")
    private val idUserKey = stringPreferencesKey("id_users")
    private val nameUserKey = stringPreferencesKey("name_user")
    private val phoneKey = stringPreferencesKey("phone_number")
    private val imagePathsKey = stringSetPreferencesKey("image_paths")
    private val abortNotifKey = booleanPreferencesKey("notification")
    private val toneKey = stringPreferencesKey("tone")

    val idAlatFlow: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[idAlatKey]
        }
    val pwdFlow: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[pwdKey]
        }
    val idUserFlow: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[idUserKey]
        }
    val nameUserFlow: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[nameUserKey]
        }
    val phoneFlow: Flow<String?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[phoneKey]
        }

    val imagePathsFlow: Flow<Set<String>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[imagePathsKey] ?: emptySet()
        }
    private val abortNotifFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[abortNotifKey] ?: false
        }
    private val toneFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { pref ->
            pref[toneKey] ?: "tone1"
        }

    val getTone: String = runBlocking {
        toneFlow.first()
    }

    fun setTone(tone: String) = runBlocking {
        context.dataStore.edit { pref ->
            pref[toneKey] = tone
        }
    }

    val getAbortNotif: Boolean = runBlocking {
        abortNotifFlow.first()
    }

    val getIdAlat : String? = runBlocking {
        idAlatFlow.first()
    }

    fun setIdAlat(idAlat: String) = runBlocking {
        context.dataStore.edit { pref ->
            pref[idAlatKey] = idAlat
        }
    }

    val getPwd : String? = runBlocking {
        pwdFlow.first()
    }

    fun setPwd(pwd: String) = runBlocking {
        context.dataStore.edit { pref ->
            pref[pwdKey] = pwd
        }
    }

    val getIdUser : String? = runBlocking {
        idUserFlow.first()
    }

    fun setIdUser(idUser: String) = runBlocking {
        context.dataStore.edit { pref ->
            pref[idUserKey] = idUser
        }
    }

    val getNameUser : String? = runBlocking {
        nameUserFlow.first()
    }

    fun setNameUser(nameUser: String) = runBlocking {
        context.dataStore.edit { pref ->
            pref[nameUserKey] = nameUser
        }
    }

    val getPhone : String? = runBlocking {
        phoneFlow.first()
    }

    fun setPhone(phone: String) = runBlocking {
        context.dataStore.edit { pref ->
            pref[phoneKey] = phone
        }
    }

    fun setPermissionNotif(notification: Boolean) = runBlocking {
        context.dataStore.edit { pref ->
            pref[abortNotifKey] = notification
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
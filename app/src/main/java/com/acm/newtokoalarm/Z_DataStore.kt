package com.acm.newtokoalarm

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore(name = "preferences")

class Z_DataStore(private val context: Context) {
    private val  appDataKey = stringPreferencesKey("appdata")
}
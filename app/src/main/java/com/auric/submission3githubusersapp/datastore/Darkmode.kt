package com.auric.submission3githubusersapp.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Darkmode private constructor(private val dataStore: DataStore<Preferences>) {
    private val THEME_KEY = booleanPreferencesKey("theme_setting")
    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: false
    }
}

suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
    dataStore.edit { preferences ->
        preferences[THEME_KEY] = isDarkModeActive
    }
}

    companion object {
        @Volatile
        private var INSTANCE: Darkmode? = null
        fun getInstance(dataStore: DataStore<Preferences>): Darkmode {
            return INSTANCE ?: synchronized(this) {
                val instance = Darkmode(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
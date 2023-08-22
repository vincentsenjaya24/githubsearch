package com.example.githubsearch.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ModePreferences private constructor(private val dataStore: DataStore<Preferences>) {


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
        private var INSTANCE: ModePreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): ModePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = ModePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val THEME_KEY = booleanPreferencesKey("theme_setting")

    }

}
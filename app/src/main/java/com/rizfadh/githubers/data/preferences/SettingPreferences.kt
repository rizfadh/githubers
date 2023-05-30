package com.rizfadh.githubers.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val DARKMODE_KEY = booleanPreferencesKey("darkmode_key")

    fun getDarkModeSettings() = dataStore.data.map {
        it[DARKMODE_KEY] ?: false
    }

    suspend fun saveDarkModeSettings(isActive: Boolean) {
        dataStore.edit {
            it[DARKMODE_KEY] = isActive
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this@Companion) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
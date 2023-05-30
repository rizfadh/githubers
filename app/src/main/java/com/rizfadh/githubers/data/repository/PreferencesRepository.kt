package com.rizfadh.githubers.data.repository

import com.rizfadh.githubers.data.preferences.SettingPreferences

class PreferencesRepository private constructor(private val preferences: SettingPreferences) {

    fun getDarkModeSettings() = preferences.getDarkModeSettings()

    suspend fun saveDarModeSettings(isActive: Boolean) = preferences.saveDarkModeSettings(isActive)

    companion object {

        @Volatile
        private var INSTANCE: PreferencesRepository? = null

        fun getInstance(preferences: SettingPreferences) =
            INSTANCE ?: synchronized(this@Companion) {
                INSTANCE ?: PreferencesRepository(preferences)
            }.also {
                INSTANCE = it
            }
    }

}
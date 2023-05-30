package com.rizfadh.githubers.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rizfadh.githubers.data.api.retrofit.ApiConfig
import com.rizfadh.githubers.data.local.room.FavoriteDatabase
import com.rizfadh.githubers.data.preferences.SettingPreferences
import com.rizfadh.githubers.data.repository.PreferencesRepository
import com.rizfadh.githubers.data.repository.UserRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        return UserRepository.getInstance(apiService, dao)
    }

    fun providePreferencesRepository(context: Context): PreferencesRepository {
        val dataStore = context.dataStore
        val preferences = SettingPreferences.getInstance(dataStore)
        return PreferencesRepository.getInstance(preferences)
    }
}
package com.rizfadh.githubers.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rizfadh.githubers.data.repository.PreferencesRepository
import com.rizfadh.githubers.data.repository.UserRepository
import com.rizfadh.githubers.di.Injection
import com.rizfadh.githubers.ui.detail.DetailViewModel
import com.rizfadh.githubers.ui.favorite.FavoriteViewModel
import com.rizfadh.githubers.ui.main.MainViewModel

class ViewModelFactory private constructor(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                return MainViewModel(userRepository, preferencesRepository) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                return DetailViewModel(userRepository) as T
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) ->
                return FavoriteViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideUserRepository(context),
                    Injection.providePreferencesRepository(context)
                )
            }.also {
                INSTANCE = it
            }
    }
}
package com.rizfadh.githubers.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.rizfadh.githubers.data.local.entity.FavoriteEntity
import com.rizfadh.githubers.data.repository.UserRepository
import com.rizfadh.githubers.utils.Result

class FavoriteViewModel(private val repository: UserRepository) : ViewModel() {

    fun getUserFavorite(): LiveData<Result<List<FavoriteEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response: LiveData<Result<List<FavoriteEntity>>> = repository.getUserFavorite().map {
                Result.Success(it)
            }
            emitSource(response)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }
}
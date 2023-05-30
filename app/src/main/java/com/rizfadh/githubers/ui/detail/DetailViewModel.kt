package com.rizfadh.githubers.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rizfadh.githubers.data.api.response.UserDetail
import com.rizfadh.githubers.data.api.response.UserItem
import com.rizfadh.githubers.data.local.entity.FavoriteEntity
import com.rizfadh.githubers.data.repository.UserRepository
import com.rizfadh.githubers.utils.Result
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository): ViewModel() {

    private val _userDetail = MutableLiveData<Result<UserDetail>>()
    val userDetail: LiveData<Result<UserDetail>> = _userDetail
    private val _userFollow = MutableLiveData<Result<List<UserItem>>>()
    val userFollow = _userFollow
    private val _isUserFavorite = MutableLiveData<FavoriteEntity>()
    val isUserFavorite: LiveData<FavoriteEntity> = _isUserFavorite

    fun getUser(username: String) {
        _userDetail.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = repository.getUser(username)
                _userDetail.value = Result.Success(response)
            } catch (e: Exception) {
                _userDetail.value = Result.Error(e.message.toString())
            }
        }
    }

    fun isFavorite(username: String) {
        viewModelScope.launch {
            val favorite = repository.isFavorite(username)
            _isUserFavorite.value = favorite
        }
    }

    fun getUserFollow(username: String, followType: String) {
        _userFollow.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = repository.getUserFollow(username, followType)
                _userFollow.value = Result.Success(response)
            } catch (e: Exception) {
                _userFollow.value = Result.Error(e.message.toString())
            }
        }
    }

    fun insertUserFavorite(user: FavoriteEntity) {
        viewModelScope.launch {
            repository.insertUserFavorite(user)
        }
    }

    fun deleteUserFavorite(user: FavoriteEntity) {
        viewModelScope.launch {
            repository.deleteUserFavorite(user)
        }
    }
}
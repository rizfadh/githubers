package com.rizfadh.githubers.ui.main

import androidx.lifecycle.*
import com.rizfadh.githubers.data.api.response.UserItem
import com.rizfadh.githubers.data.repository.PreferencesRepository
import com.rizfadh.githubers.data.repository.UserRepository
import com.rizfadh.githubers.utils.Result
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {


    private val _userList = MutableLiveData<Result<List<UserItem>>>()
    val userList: LiveData<Result<List<UserItem>>> = _userList

    init {
        searchUser("rizky")
    }

    fun searchUser(username: String) {
        _userList.value = Result.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.searchUser(username)
                _userList.value = Result.Success(response.userList)
            } catch (e: Exception) {
                _userList.value = Result.Error(e.message.toString())
            }
        }
    }

    fun getDarkModeSettings(): LiveData<Boolean> = preferencesRepository.getDarkModeSettings().asLiveData()

    fun saveDarkModeSettings(isActive: Boolean) {
        viewModelScope.launch {
            preferencesRepository.saveDarModeSettings(isActive)
        }
    }
}
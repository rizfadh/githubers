package com.rizfadh.githubers.data.repository

import com.rizfadh.githubers.data.api.retrofit.ApiService
import com.rizfadh.githubers.data.local.entity.FavoriteEntity
import com.rizfadh.githubers.data.local.room.FavoriteDao

class UserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao
) {

    suspend fun searchUser(username: String) = apiService.searchUser(username)

    suspend fun getUser(username: String) = apiService.getUser(username)

    suspend fun getUserFollow(username: String, followType: String) =
        apiService.getUserFollow(username, followType)

    fun getUserFavorite() = favoriteDao.getFavorite()

    suspend fun isFavorite(username: String) = favoriteDao.isFavorite(username)

    suspend fun insertUserFavorite(user: FavoriteEntity) = favoriteDao.insertFavorite(user)

    suspend fun deleteUserFavorite(user: FavoriteEntity) = favoriteDao.delete(user)

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteDao
        ) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserRepository(apiService, favoriteDao)
            }.also {
                INSTANCE = it
            }
    }
}
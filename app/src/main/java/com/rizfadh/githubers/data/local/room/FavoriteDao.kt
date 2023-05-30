package com.rizfadh.githubers.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rizfadh.githubers.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * FROM favorite WHERE login = :username")
    suspend fun isFavorite(username: String): FavoriteEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)
}
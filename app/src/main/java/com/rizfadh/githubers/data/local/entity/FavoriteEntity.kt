package com.rizfadh.githubers.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteEntity(
    @ColumnInfo(name = "login")
    @PrimaryKey
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String
)

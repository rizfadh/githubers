package com.rizfadh.githubers.data.api.retrofit

import com.rizfadh.githubers.data.api.response.UserDetail
import com.rizfadh.githubers.data.api.response.UserItem
import com.rizfadh.githubers.data.api.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun searchUser(@Query("q") username: String): UserResponse

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): UserDetail

    @GET("users/{username}/{followType}")
    suspend fun getUserFollow(
        @Path("username") username: String,
        @Path("followType") followType: String
    ): List<UserItem>
}
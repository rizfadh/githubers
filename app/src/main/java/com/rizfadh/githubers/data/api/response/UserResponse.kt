package com.rizfadh.githubers.data.api.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("items")
	val userList: List<UserItem>
)

data class UserItem(

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("login")
	val login: String
)

data class UserDetail(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("public_repos")
	val publicRepos: Int,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("following")
	val following: Int
)
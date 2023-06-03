package com.riski.mystorylife.data.model

import com.google.gson.annotations.SerializedName

data class UserDataStory(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("token")
	val token: String,

	var isLogin: Boolean
)

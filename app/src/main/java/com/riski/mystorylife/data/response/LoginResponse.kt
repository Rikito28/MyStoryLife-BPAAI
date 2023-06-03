package com.riski.mystorylife.data.response

import com.google.gson.annotations.SerializedName
import com.riski.mystorylife.data.model.UserDataStory

data class LoginResponse(

	@field:SerializedName("error")
	var error: Boolean,

	@field:SerializedName("message")
	var message: String,

	@field:SerializedName("loginResult")
	var loginResult: UserDataStory?
)

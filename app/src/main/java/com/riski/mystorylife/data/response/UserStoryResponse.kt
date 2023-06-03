package com.riski.mystorylife.data.response

import com.google.gson.annotations.SerializedName
import com.riski.mystorylife.data.model.ListUserStory

data class UserStoryResponse(

	@field:SerializedName("listStory")
	val listStory: List<ListUserStory>,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
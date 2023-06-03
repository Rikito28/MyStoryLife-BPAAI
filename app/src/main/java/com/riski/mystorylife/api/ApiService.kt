package com.riski.mystorylife.api

import com.riski.mystorylife.data.response.UserStoryResponse
import com.riski.mystorylife.data.response.LoginResponse
import com.riski.mystorylife.data.response.NewStoryResponse
import com.riski.mystorylife.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part("description") desk: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<NewStoryResponse>

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") auth: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0,
    ): UserStoryResponse

    @GET("stories")
    fun getLocation(
        @Header("Authorization") auth: String,
        @Query("size") size: Int = 25,
        @Query("location") location: Int = 1,
    ): Call<UserStoryResponse>
}
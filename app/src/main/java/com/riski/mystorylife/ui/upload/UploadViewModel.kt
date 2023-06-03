package com.riski.mystorylife.ui.upload

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.riski.mystorylife.api.ApiConfig
import com.riski.mystorylife.data.model.UserDataStory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.data.response.NewStoryResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(private val pref: Preference): ViewModel() {
    private val _upload = MutableLiveData<NewStoryResponse>()
    val upload: LiveData<NewStoryResponse> = _upload

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    fun getUser() : LiveData<UserDataStory> {
        return pref.getLogin().asLiveData()
    }

    fun uploadImg(token: String, description: RequestBody, imgMultipart: MultipartBody.Part) {
        _loading.value = true
        val  api = ApiConfig.getApiService().addNewStory("Bearer $token", description, imgMultipart)
        api.enqueue(object : Callback<NewStoryResponse> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<NewStoryResponse>, response: Response<NewStoryResponse> ) {
                if (response.isSuccessful) {
                    _upload.value = response.body()
                } else {
                    val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
                    _upload.value =
                         NewStoryResponse(jsonObject.getBoolean("error"), jsonObject.getString("message"))
                            Log.e(TAG, "onFailure: ${response.message()}")
                }
                }

            override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        const val TAG = "UploadViewModel"
    }
}
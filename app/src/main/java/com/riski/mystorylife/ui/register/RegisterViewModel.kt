package com.riski.mystorylife.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.mystorylife.api.ApiConfig
import com.riski.mystorylife.data.response.RegisterResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _response = MutableLiveData<RegisterResponse?>()
    val response: LiveData<RegisterResponse?> = _response

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun register(name: String, email: String, password: String) {
        _loading.value = true
        _response.value = null
        val api = ApiConfig.getApiService().register(name, email, password)
        api.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response1: Response<RegisterResponse>) {
                _loading.value = false
                if (response1.isSuccessful) {
                    _response.value = response1.body()
                } else {
                    val jsonObject = JSONObject(response1.errorBody()!!.charStream().readText())
                    _response.value = RegisterResponse(jsonObject.getBoolean("error"), jsonObject.getString("message"))
                    Log.e(TAG, "onFailure: ${response1.message()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        const val TAG = "RegisterViewModel"
    }
}
package com.riski.mystorylife.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riski.mystorylife.api.ApiConfig
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.data.response.LoginResponse
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel (private val preference: Preference) : ViewModel() {

    private val _response = MutableLiveData<LoginResponse?>()
    val response: LiveData<LoginResponse?> = _response
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun login(email: String, password: String) {
        _loading.value = true
        _response.value = null

        val service = ApiConfig().getApiService().login(email, password)
        service.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response2: Response<LoginResponse>) {
                _loading.value = false
                if (response2.isSuccessful) {
                    _response.value = response2.body()
                    viewModelScope.launch {
                        response2.body()?.loginResult?.isLogin = true
                        response2.body()?.loginResult?.let { preference.saveLogin(it) }
                    }
                } else {
                    val jsonObj =
                        JSONObject(response2.errorBody()!!.charStream().readText())
                            _response.value = LoginResponse(
                                jsonObj.getBoolean("error"),
                                jsonObj.getString("message"),
                                null
                            )
                    Log.e(TAG, "onFailure: ${response2.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
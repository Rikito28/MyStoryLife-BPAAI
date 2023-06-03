package com.riski.mystorylife.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.riski.mystorylife.api.ApiConfig
import com.riski.mystorylife.data.StoryRepository
import com.riski.mystorylife.data.model.UserDataStory
import com.riski.mystorylife.data.model.ListUserStory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.data.response.UserStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val mainViewModel: StoryRepository, private val pref: Preference) : ViewModel() {

    fun getStory(token: String) = mainViewModel.getAllStories("Bearer $token").asLiveData()
    private val _storyMain = MutableLiveData<List<ListUserStory>>()
    val storyMain : LiveData<List<ListUserStory>> = _storyMain

    private val _getData = MutableLiveData<Boolean>()

    private val _isLoading = MutableLiveData<Boolean>()

    fun getUser(): LiveData<UserDataStory> {
        return pref.getLogin().asLiveData()
    }

    fun getLocation(token : String) {
        _isLoading.value = true
        _getData.value = true
        val client = ApiConfig.getApiService().getLocation("Bearer $token")
        client.enqueue(object : Callback<UserStoryResponse> {
            override fun onResponse(call: Call<UserStoryResponse>, response: Response<UserStoryResponse> ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val response1 = response.body()
                    if (response1 != null) {
                        if (!response1.error) {
                            _storyMain.value = response.body()?.listStory
                            _getData.value = response1.message == "Story Successfully"
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserStoryResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}

private fun <T> LiveData<T>.asLiveData(): LiveData<T> {
    return this
}

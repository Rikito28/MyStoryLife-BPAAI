package com.riski.mystorylife

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.riski.mystorylife.data.model.UserDataStory
import com.riski.mystorylife.data.model.Preference
import kotlinx.coroutines.launch

class AuthViewModel(private val preference: Preference): ViewModel() {

    fun getUser(): LiveData<UserDataStory> {
        return preference.getLogin().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            preference.userLogout()
        }
    }
}
package com.riski.mystorylife

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riski.mystorylife.data.inject.Injection
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.ui.login.LoginViewModel
import com.riski.mystorylife.ui.main.MainViewModel
import com.riski.mystorylife.ui.register.RegisterViewModel
import com.riski.mystorylife.ui.upload.UploadViewModel

class ViewModelFactory (private val preferences: Preference, private val context : Context): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel() as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(preferences) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(Injection.provideRepository(context), preferences) as T
            }
            else -> throw  IllegalArgumentException("Unknown ViewModel" + modelClass.name)

        }
    }
}
package com.riski.mystorylife.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Preference private constructor(private val dataStore: DataStore<Preferences>){

    fun getLogin(): Flow<UserDataStory> {
        return dataStore.data.map {
            UserDataStory(
                it[USERID_KEY] ?: "",
                it[NAME_KEY] ?: "",
                it[TOKEN_KEY] ?: "",
                it[isLogin] ?: false
            )
        }
    }

    suspend fun login() {
        dataStore.edit {
            it[isLogin] = true
        }
    }

    suspend fun saveLogin(user: UserDataStory) {
        dataStore.edit {
            it[USERID_KEY] = user.userId
            it[NAME_KEY] = user.name
            it[TOKEN_KEY] = user.token
            it[isLogin] = user.isLogin
        }
    }

    suspend fun userLogout() {
        dataStore.edit {
            it[USERID_KEY] = ""
            it[NAME_KEY] = ""
            it[TOKEN_KEY] = ""
            it[isLogin] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Preference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val USERID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val isLogin = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): Preference {
            return INSTANCE ?: synchronized(this) {
                val instance = Preference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
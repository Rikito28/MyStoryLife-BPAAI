package com.riski.mystorylife.data.inject

import android.content.Context
import com.riski.mystorylife.api.ApiConfig
import com.riski.mystorylife.data.StoryRepository
import com.riski.mystorylife.data.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context) : StoryRepository {
        val db = StoryDatabase.getDb(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(db, apiService)
    }
}
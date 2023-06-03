package com.riski.mystorylife.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.riski.mystorylife.api.ApiService
import com.riski.mystorylife.data.database.StoryDatabase
import com.riski.mystorylife.data.model.ListStory

class StoryRepository(private val database: StoryDatabase, private val apiService: ApiService) {
    fun getAllStories(token: String): LiveData<PagingData<ListStory>> {
    @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
                remoteMediator = StoryRemoteMediator(database, apiService, token), pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
            ).liveData
    }
}
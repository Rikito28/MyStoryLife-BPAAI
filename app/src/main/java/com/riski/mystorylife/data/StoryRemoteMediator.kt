package com.riski.mystorylife.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.riski.mystorylife.api.ApiService
import com.riski.mystorylife.data.database.RemoteKey
import com.riski.mystorylife.data.database.StoryDatabase
import com.riski.mystorylife.data.model.ListStory

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(private val db: StoryDatabase, private val apiservice: ApiService, private val token: String): RemoteMediator<Int, ListStory>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStory>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val key = getRemoteKeyClosestToCurrentPosition(state)
                key?.nextKey?.minus(1) ?: initialPageIndex
            }
            LoadType.PREPEND -> {
                val key = getRemoteKeyForFirstItem(state)
                val prevKey = key?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = key != null)
                prevKey
            }
            LoadType.APPEND -> {
                val key = getRemoteKeyForLastItem(state)
                val nextkey = key?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = key != null)
                nextkey
            }
        }

        return try {
            val responseData = apiservice.getAllStory(token, page, state.config.pageSize)
            val endofPagginationReached = responseData.listStory.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeyDao().deleteRemoteKey()
                    db.storyDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endofPagginationReached) null else page + 1
                val key = responseData.listStory.map {
                    RemoteKey(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeyDao().insertALl(key)
                responseData.listStory.forEach { story ->
                    val item = ListStory(
                        story.id,
                        story.name,
                        story.description,
                        story.photoUrl,
                        story.createdAt,
                        story.lat,
                        story.lon
                    )
                    db.storyDao().insertStory(item)
                }
            }
            MediatorResult.Success(endOfPaginationReached = endofPagginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStory>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            db.remoteKeyDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStory>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.remoteKeyDao().getRemoteKeysId(id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStory>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            db.remoteKeyDao().getRemoteKeysId(data.id)
        }
    }

    companion object {
        const val initialPageIndex = 1
    }
}
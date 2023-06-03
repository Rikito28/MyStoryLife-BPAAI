package com.riski.mystorylife

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.riski.mystorylife.data.model.ListStory

class paggingData : PagingSource<Int, ListStory>() {
    override fun getRefreshKey(state: PagingState<Int, ListStory>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStory> =
        LoadResult.Page(emptyList(),0,1)

    companion object {
        fun snapshot(items: List<ListStory>): PagingData<ListStory> {
            return PagingData.from(items)
        }
    }
}
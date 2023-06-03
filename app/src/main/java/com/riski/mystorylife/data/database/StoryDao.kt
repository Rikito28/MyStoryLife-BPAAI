package com.riski.mystorylife.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riski.mystorylife.data.model.ListStory

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(listStory: ListStory)

    @Query("SELECT * FROM liststory")
    fun getAllStory(): PagingSource<Int, ListStory>

    @Query("DELETE FROM liststory")
    suspend fun deleteAll()
}
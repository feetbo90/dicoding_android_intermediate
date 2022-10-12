package com.my.dicoding_android_intermediate.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.my.dicoding_android_intermediate.data.remote.model.StoryLocalModel

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(quote: List<StoryLocalModel>)

    @Query("SELECT * FROM theStory")
    fun getAllStories(): PagingSource<Int, StoryLocalModel>

    @Query("DELETE FROM theStory")
    suspend fun deleteAll()
}
package com.my.dicoding_android_intermediate.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.my.dicoding_android_intermediate.data.remote.model.StoryRemoteKey
import com.my.dicoding_android_intermediate.utils.Utils

@Dao
interface StoryRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<StoryRemoteKey>)

    @Query("SELECT * FROM ${Utils.STORY_REMOTE_KEY} WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): StoryRemoteKey?

    @Query("DELETE FROM ${Utils.STORY_REMOTE_KEY}")
    suspend fun deleteRemoteKeys()
}
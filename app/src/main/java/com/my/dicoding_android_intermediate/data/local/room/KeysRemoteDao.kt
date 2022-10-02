package com.my.dicoding_android_intermediate.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.my.dicoding_android_intermediate.data.local.entity.KeysRemote

@Dao
interface KeysRemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keysRemote: List<KeysRemote>)

    @Query("SELECT * FROM keysRemote WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): KeysRemote?

    @Query("DELETE FROM keysRemote")
    suspend fun deleteRemoteKeys()
}
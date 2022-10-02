package com.my.dicoding_android_intermediate.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.dicoding_android_intermediate.data.local.entity.KeysRemote
import com.my.dicoding_android_intermediate.data.local.entity.Story

@Database(
    entities = [Story::class, KeysRemote::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDb : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun keysRemoteDao(): KeysRemoteDao
}
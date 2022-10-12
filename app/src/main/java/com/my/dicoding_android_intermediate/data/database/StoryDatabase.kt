package com.my.dicoding_android_intermediate.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.my.dicoding_android_intermediate.data.remote.model.StoryLocalModel

@Database(
    entities = [StoryLocalModel::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao

    abstract fun storyRemoteKeysDao(): StoryRemoteKeysDao
}
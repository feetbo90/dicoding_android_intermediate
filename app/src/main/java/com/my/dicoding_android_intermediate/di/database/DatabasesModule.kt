package com.my.dicoding_android_intermediate.di.database

import android.content.Context
import androidx.room.Room
import com.my.dicoding_android_intermediate.data.local.room.KeysRemoteDao
import com.my.dicoding_android_intermediate.data.local.room.StoryDao
import com.my.dicoding_android_intermediate.data.local.room.StoryDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabasesModule {

    @Provides
    fun provideStoryDao(storyDatabase: StoryDb): StoryDao = storyDatabase.storyDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: StoryDb): KeysRemoteDao =
        storyDatabase.keysRemoteDao()

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext context: Context): StoryDb {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryDb::class.java,
            "story_database"
        ).build()
    }
}
package com.my.dicoding_android_intermediate.di.database

import android.content.Context
import androidx.room.Room
import com.my.dicoding_android_intermediate.data.database.StoryDao
import com.my.dicoding_android_intermediate.data.database.StoryDatabase
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.utils.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao = storyDatabase.storyDao()

//    @Provides
//    fun provideRemoteKeysDao(storyDatabase: StoryDatabase): KeysRemoteDao =
//        storyDatabase.keysRemoteDao()

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext context: Context): StoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoryDatabase::class.java,
            Utils.DATABASE_NAME
        ).build()
    }
}
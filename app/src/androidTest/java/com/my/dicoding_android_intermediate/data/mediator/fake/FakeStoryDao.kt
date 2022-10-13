package com.my.dicoding_android_intermediate.data.mediator.fake

import androidx.paging.PagingSource
import com.my.dicoding_android_intermediate.data.database.StoryDao
import com.my.dicoding_android_intermediate.data.remote.model.StoryLocalModel

class FakeStoryDao: StoryDao {
    private var storyData = mutableListOf<StoryLocalModel>()

    override suspend fun insertStories(quote: List<StoryLocalModel>) {
        storyData.addAll(quote)
    }

    override fun getAllStories(): PagingSource<Int, StoryLocalModel> {

    }

    override suspend fun deleteAll() {
        storyData.removeAll()
    }
}
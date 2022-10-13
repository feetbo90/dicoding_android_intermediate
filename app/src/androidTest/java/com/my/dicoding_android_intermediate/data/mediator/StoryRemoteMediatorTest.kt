package com.my.dicoding_android_intermediate.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private lateinit var storyRepository: StoryRepository

}
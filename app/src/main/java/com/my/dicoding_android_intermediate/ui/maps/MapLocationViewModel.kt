package com.my.dicoding_android_intermediate.ui.maps

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MapLocationViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {
    fun getAllStories(token: String): Flow<Result<StoryResponse>> =
        storyRepository.getAllStoriesWithMapLocation(token)
}
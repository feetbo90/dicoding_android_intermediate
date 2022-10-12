package com.my.dicoding_android_intermediate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponseItem
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import com.my.dicoding_android_intermediate.data.result.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository
): ViewModel(){
    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()

    private val _stories = MutableStateFlow<MyResult<StoryResponse>>(MyResult.Loading)
    val stories = _stories.asStateFlow()

    fun getStories(token: String) {
        _stories.value = MyResult.Loading
        viewModelScope.launch {
            storyRepository.getStories(token).collect {
                _stories.value = it
            }
        }
        _isLoaded.value = true
    }

    fun setLoaded(isLoading: Boolean) {
        _isLoaded.value = isLoading
    }

    fun getStoriesTwo(token: String): LiveData<PagingData<Story>> =
        storyRepository.getStories(token).cachedIn(viewModelScope).asLiveData()

}
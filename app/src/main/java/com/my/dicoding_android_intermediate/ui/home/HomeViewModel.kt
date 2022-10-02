package com.my.dicoding_android_intermediate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.my.dicoding_android_intermediate.data.local.entity.Story
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val storyRepository: StoryRepository
): ViewModel(){
//    private val _isLoaded = MutableStateFlow(false)
//    val isLoaded = _isLoaded.asStateFlow()
//
//    private val _stories = MutableStateFlow<MyResult<StoryResponse>>(MyResult.Loading)
//    val stories = _stories.asStateFlow()

//    fun getStories(token: String) {
//        _stories.value = MyResult.Loading
//        viewModelScope.launch {
//            storyRepository.getStories(token).collect {
//                _stories.value = it
//            }
//        }
//        _isLoaded.value = true
//    }

//    fun setLoaded(isLoading: Boolean) {
//        _isLoaded.value = isLoading
//    }

    fun getStoriesTwo(token: String): LiveData<PagingData<Story>> =
        storyRepository.getStories(token).cachedIn(viewModelScope).asLiveData()

}
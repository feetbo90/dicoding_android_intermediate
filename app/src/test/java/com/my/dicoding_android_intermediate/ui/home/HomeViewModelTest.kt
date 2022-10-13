package com.my.dicoding_android_intermediate.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.my.dicoding_android_intermediate.DataDummy
import com.my.dicoding_android_intermediate.MainDispatcherRule
import com.my.dicoding_android_intermediate.adapter.StoriesListAdapter
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import com.my.dicoding_android_intermediate.ui.login.LoginFragmentViewModel
import com.my.dicoding_android_intermediate.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel = HomeViewModel(storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest{
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<Story> = QuotePagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        val newExpected = expectedStory.asFlow()

        `when`(storyRepository.getAllStories("auth_token")).thenReturn(newExpected)
        val actualStory: PagingData<Story> = homeViewModel.getStoriesTwo("auth_token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesListAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class QuotePagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}
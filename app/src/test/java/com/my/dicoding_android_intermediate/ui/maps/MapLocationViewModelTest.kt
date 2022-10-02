package com.my.dicoding_android_intermediate.ui.maps

import androidx.paging.ExperimentalPagingApi
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import com.my.dicoding_android_intermediate.test.DataTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MapLocationViewModelTest {

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mapLocationViewModel: MapLocationViewModel

    private val dummyStoriesResponse = DataTest.generateDummyStoriesResponse()
    private val dummyToken = "AUTH_TOKEN"

    @Before
    fun setup() {
        mapLocationViewModel = MapLocationViewModel(storyRepository)
    }

    @Test
    fun `Get story with location successfully - result success`(): Unit = runTest {

        val expectedResponse = flowOf(Result.success(dummyStoriesResponse))

        Mockito.`when`(mapLocationViewModel.getAllStories(dummyToken)).thenReturn(expectedResponse)

        mapLocationViewModel.getAllStories(dummyToken).collect { actualResponse ->

            Assert.assertTrue(actualResponse.isSuccess)
            Assert.assertFalse(actualResponse.isFailure)

            actualResponse.onSuccess { storiesResponse ->
                Assert.assertNotNull(storiesResponse)
                Assert.assertSame(storiesResponse, dummyStoriesResponse)
            }
        }

        Mockito.verify(storyRepository).getAllStoriesWithMapLocation(dummyToken)
    }

    @Test
    fun `Get story with location failed - result failure with exception`(): Unit = runTest {

        val expectedResponse: Flow<Result<StoryResponse>> =
            flowOf(Result.failure(Exception("Failed")))

        Mockito.`when`(mapLocationViewModel.getAllStories(dummyToken)).thenReturn(expectedResponse)

        mapLocationViewModel.getAllStories(dummyToken).collect { actualResponse ->

            Assert.assertFalse(actualResponse.isSuccess)
            Assert.assertTrue(actualResponse.isFailure)

            actualResponse.onFailure {
                Assert.assertNotNull(it)
            }
        }

        Mockito.verify(storyRepository).getAllStoriesWithMapLocation(dummyToken)
    }
}
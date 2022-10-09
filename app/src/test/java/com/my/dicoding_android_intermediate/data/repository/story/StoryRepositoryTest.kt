package com.my.dicoding_android_intermediate.data.repository.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.my.dicoding_android_intermediate.DataDummy
import com.my.dicoding_android_intermediate.MainDispatcherRule
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.result.MyResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var storyRepository: StoryRepository
    private val dummyMapStories = DataDummy.generateMapStories()
    private val dummyMapStoriesFailed = DataDummy.generateMapStoriesFailed()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        storyRepository = StoryRepository(apiService)
    }

    @Test
    fun uploadImage() {
    }

    @Test
    fun getMapStoriesSuccess() = runTest {
        `when`(apiService.getMapStories(getBearerToken("auth_token"), location = 1)).thenReturn(
            dummyMapStories
        )
        storyRepository.getMapStories("auth_token").collect { result ->
            when (result) {
                is MyResult.Success -> {
                    assertNotNull(result)
                    assertEquals(dummyMapStories, result.data)
                }
                else -> {}
            }
        }
        verify(apiService).getMapStories(getBearerToken("auth_token"), location = 1)
    }

    @Test
    fun getMapStoriesFailed() = runTest {
        `when`(
            apiService.getMapStories(
                getBearerToken("auth_token"),
                location = 1
            )
        ).then { dummyMapStoriesFailed }
        storyRepository.getMapStories("auth_token").collect { result ->
            assertNotNull(result)
            when(result) {
                is MyResult.Error -> {
                    assertEquals(dummyMapStoriesFailed.error, result.error)
                    assertTrue(dummyMapStoriesFailed == result)
                }
                else -> {}
            }
        }
        verify(apiService).getMapStories(getBearerToken("auth_token"), location = 1)
    }

    private fun getBearerToken(token: String): String {
        return "Bearer $token"
    }
}
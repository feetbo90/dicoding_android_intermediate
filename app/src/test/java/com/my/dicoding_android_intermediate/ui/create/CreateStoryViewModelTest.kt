package com.my.dicoding_android_intermediate.ui.create

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.my.dicoding_android_intermediate.DataDummy
import com.my.dicoding_android_intermediate.MainDispatcherRule
import com.my.dicoding_android_intermediate.data.database.StoryDatabase
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import com.my.dicoding_android_intermediate.data.result.MyResult
import com.my.dicoding_android_intermediate.ui.login.LoginFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CreateStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var createStoryViewModel: CreateStoryViewModel
    private val dummyMultipart = DataDummy.createMultiPart()
    private val dummyDescription = DataDummy.createRequestBody("description")
    private val dummyLat = DataDummy.createRequestBody("12.0")
    private val dummyLon = DataDummy.createRequestBody("99.0")
    private val dummyFileUpload = DataDummy.generateUploadFile()
    private val dummyFileUploadFailed = DataDummy.getUploadFailed()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        createStoryViewModel = CreateStoryViewModel(authRepository, storyRepository)
    }

    @Test
    fun `File upload should not null and return success`() = runTest {
        val expectedFileUpload = flow {
            emit(Result.success(dummyFileUpload))
        }
        `when`(storyRepository.uploadImage("auth_token", dummyMultipart, dummyDescription, dummyLat, dummyLon)).thenReturn(expectedFileUpload)
        createStoryViewModel.uploadImage("auth_token", dummyMultipart, dummyDescription, dummyLat, dummyLon).collect { result ->
            result.onSuccess {
                assertTrue(result.isSuccess)
            }
        }
        Mockito.verify(storyRepository).uploadImage("auth_token", dummyMultipart, dummyDescription, dummyLat, dummyLon)
    }

    @Test
    fun `File upload should failed`() = runTest {
        val expectedLogin = flow {
            emit(Result.(dummyFileUploadFailed))
        }

        `when`(storyRepository.uploadImage("auth_token", dummyMultipart, dummyDescription, dummyLat, dummyLon)).then { dummyFileUploadFailed }
        createStoryViewModel.uploadImage("auth_token", dummyMultipart, dummyDescription, dummyLat, dummyLon).collect { result ->
            result.onFailure {
                assertTrue(result.isFailure)
            }
        }
        Mockito.verify(storyRepository).uploadImage("auth_token", dummyMultipart, dummyDescription, dummyLat, dummyLon)
    }

}
package com.my.dicoding_android_intermediate.data.repository.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.my.dicoding_android_intermediate.data.database.StoryDatabase
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.data.mediator.StoryRemoteMediator
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.remote.response.ResponseFileUpload
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.result.MyResult
import com.my.dicoding_android_intermediate.data.sources.StoryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {
    fun getStories(token: String): Flow<MyResult<StoryResponse>> = flow {
        emit(MyResult.Loading)
        try {
            val response = apiService.getAllStories(getBearerToken(token))
            emit(MyResult.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(MyResult.Error(e.toString()))
        }
    }

    @ExperimentalPagingApi
    fun getAllStories(): Flow<PagingData<Story>> = flow {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = StoryRemoteMediator(apiService, storyDatabase, ""),
            pagingSourceFactory = {
                StoryPagingSource
            }
        )
    }

    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<Result<ResponseFileUpload>> = flow {
        try {
            val bearerToken = getBearerToken(token)
            val response = apiService.uploadImage(bearerToken, file, description, lat, lon)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun getMapStories(token: String): Flow<MyResult<StoryResponse>> = flow {
        emit(MyResult.Loading)
        try {
            val response = apiService.getMapStories(getBearerToken(token), location = 1)
            emit(MyResult.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(MyResult.Error(e.toString()))
        }
    }

    private fun getBearerToken(token: String): String {
        return "Bearer $token"
    }
}
package com.my.dicoding_android_intermediate.data.repository.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.my.dicoding_android_intermediate.data.local.entity.Story
import com.my.dicoding_android_intermediate.data.local.room.StoryDb
import com.my.dicoding_android_intermediate.data.remote.mediator.StoryMediator
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.remote.response.ResponseFileUpload
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.result.MyResult
import com.my.dicoding_android_intermediate.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ExperimentalPagingApi
class StoryRepository @Inject constructor(
    private val storyDb: StoryDb,
    private val apiService: ApiService,
) {
    fun getStoriesBefore(token: String): Flow<MyResult<StoryResponse>> = flow {
        emit(MyResult.Loading)
        try {
            val response = apiService.getAllStories(getBearerToken(token))
            emit(MyResult.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(MyResult.Error(e.toString()))
        }
    }

    fun getStories(token: String): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = StoryMediator(
                storyDb,
                apiService,
                getBearerToken(token)
            ),
            pagingSourceFactory = {
                storyDb.storyDao().getAllStories()
            }
        ).flow
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

    private fun getBearerToken(token: String): String = "Bearer $token"

    fun getAllStoriesWithMapLocation(token: String): Flow<Result<StoryResponse>> = flow {
        wrapEspressoIdlingResource {
            try {
                val bearerToken = getBearerToken(token)
                val response = apiService.getAllStories(bearerToken, size = 30, location = 1)
                emit(Result.success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.failure(e))
            }
        }
    }
}
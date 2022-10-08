package com.my.dicoding_android_intermediate.data.repository.story

import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.remote.response.ResponseFileUpload
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import com.my.dicoding_android_intermediate.data.result.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
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
            val response = apiService.getAllStories(getBearerToken(token), location = 1)
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
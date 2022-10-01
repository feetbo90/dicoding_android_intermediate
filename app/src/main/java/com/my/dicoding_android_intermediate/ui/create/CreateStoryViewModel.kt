package com.my.dicoding_android_intermediate.ui.create

import androidx.lifecycle.ViewModel
import com.my.dicoding_android_intermediate.data.remote.response.ResponseFileUpload
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import com.my.dicoding_android_intermediate.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class CreateStoryViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storyRepository: StoryRepository
): ViewModel(){
    fun getAuthToken(): Flow<String?> = authRepository.getAuthToken()

    suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<Result<ResponseFileUpload>> =
        storyRepository.uploadImage(token, file, description, lat, lon)
}
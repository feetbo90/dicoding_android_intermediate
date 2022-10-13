package com.my.dicoding_android_intermediate.data.mediator.fake

import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.remote.response.ResponseFileUpload
import com.my.dicoding_android_intermediate.data.remote.response.ResponseLogin
import com.my.dicoding_android_intermediate.data.remote.response.ResponseRegister
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiServices : ApiService {
    override suspend fun loginUser(email: String, password: String): ResponseLogin {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): ResponseRegister {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): StoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): ResponseFileUpload {
        TODO("Not yet implemented")
    }

    override suspend fun getMapStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): StoryResponse {
        TODO("Not yet implemented")
    }
}
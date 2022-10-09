package com.my.dicoding_android_intermediate

import com.my.dicoding_android_intermediate.data.remote.response.*
import com.my.dicoding_android_intermediate.data.result.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlin.ClassCastException
import kotlin.Exception

object DataDummy {

    fun generateDummyLogin(): ResponseLogin {
        val userLogin =
            LoginResult(name = "Muhammad Iqbal Pradipta", userId = "123456", token = "auth_token")
        return ResponseLogin(userLogin, error = false, message = "Berhasil Login")
    }

    fun generateFailedLogin(): Exception {
        return Exception("Login Failed")
    }

    fun generateDummyRegister(): ResponseRegister {
        return ResponseRegister(error = false, message = "Berhasil Register")
    }

    fun generateFailedRegister(): Exception {
        return Exception("Register Failed")
    }

    fun getAuthToken(): Flow<String> {
        return flowOf("auth_token")
    }

    fun generateUploadFile(): ResponseFileUpload {
        return ResponseFileUpload(error = false, message = "Berhasil Upload")
    }

    fun generateMapStories(): StoryResponse {
        val storyItem1 = StoryResponseItem(
            photoUrl = "coba.jpg",
            createdAt = "19-08-1990",
            name = "Joko",
            description = "ini description",
            lon = 19.0,
            lat = 20.0,
            id = "123"
        )
        val storyItem2 = StoryResponseItem(
            photoUrl = "coba1.jpg",
            createdAt = "20-08-1990",
            name = "Joko2",
            description = "ini description2",
            lon = 19.0,
            lat = 20.0,
            id = "1234"
        )
        val list = arrayListOf<StoryResponseItem>()
        list.add(storyItem1)
        list.add(storyItem2)
        return StoryResponse(list, error = false, message = "Berhasil ambil data map")
    }

    fun generateMapStoriesFailed(): MyResult.Error {
        return MyResult.Error(ClassCastException("class com.my.dicoding_android_intermediate.data.result.MyResult\$Error cannot be cast to class com.my.dicoding_android_intermediate.data.remote.response.StoryResponse (com.my.dicoding_android_intermediate.data.result.MyResult\$Error and com.my.dicoding_android_intermediate.data.remote.response.StoryResponse are in unnamed module of loader 'app')").toString())
//        return MyResult.Error(ClassCastException("error").toString())
    }

    fun getMapStoriesError(): Exception {
        return Exception(ClassCastException())
    }
}
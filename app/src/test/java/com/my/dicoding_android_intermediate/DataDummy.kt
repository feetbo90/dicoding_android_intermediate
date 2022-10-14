package com.my.dicoding_android_intermediate

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.data.remote.model.StoryLocalModel
import com.my.dicoding_android_intermediate.data.remote.response.*
import com.my.dicoding_android_intermediate.data.result.MyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DataDummy {

    fun generateDummyLogin(): ResponseLogin {
        val userLogin =
            LoginResult(name = "Muhammad Iqbal Pradipta", userId = "123456", token = "auth_token")
        return ResponseLogin(userLogin, error = false, message = "Berhasil Login")
    }

    fun generateDummyLoginMyResult(): Flow<MyResult<ResponseLogin>> = flow {
        val userLogin =
            LoginResult(name = "Muhammad Iqbal Pradipta", userId = "123456", token = "auth_token")
        val myResponse = ResponseLogin(userLogin, error = false, message = "Berhasil Login")
        emit(MyResult.Success(myResponse))
    }

    fun generateFailedLogin(): MyResult.ErrorException {
        return MyResult.ErrorException(Exception("Login Failed"))
    }

    fun generateDummyRegister(): ResponseRegister {
        return ResponseRegister(error = false, message = "Berhasil Register")
    }

    fun generateFailedRegister(): MyResult.ErrorException {
        return MyResult.ErrorException(Exception("Register Failed"))
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

    fun getUploadFailed(): Exception {
        return Exception("error upload")
    }

    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z")
        for (i in 0..100) {
            val quote = Story(
                i.toString(),
                "author + $i.png",
                ZonedDateTime.now(),
                "Mhd Iqbal Pradipta",
                "deskripsi",
                12.2,
                19.0
            )
            items.add(quote)
        }
        return items
    }

    fun generateDummyStoryLocalModelResponse(): List<StoryLocalModel> {
        val items: MutableList<StoryLocalModel> = arrayListOf()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z")
        for (i in 0..100) {
            val quote = StoryLocalModel(
                i.toString(),
                "author + $i.png",
                "Description",
                "author + $i.png",
                ZonedDateTime.now().toString(),
                12.2,
                19.0
            )
            items.add(quote)
        }
        return items
    }

    fun createMultiPart(): MultipartBody.Part {
        val file =
            "Hallo data"
                .toRequestBody("text/plain".toMediaType())
        return MultipartBody.Part.createFormData(
            "photo",
            "Hallo.jpg",
            file
        )
    }

    fun createRequestBody(data : String): RequestBody = data.toRequestBody("text/plain".toMediaType())


}
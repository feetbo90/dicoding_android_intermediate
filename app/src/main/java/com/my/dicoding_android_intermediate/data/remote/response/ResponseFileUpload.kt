package com.my.dicoding_android_intermediate.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseFileUpload(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

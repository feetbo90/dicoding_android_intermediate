package com.my.dicoding_android_intermediate.data.result

sealed class MyResult<out R> private constructor() {
    data class Success<out T>(val data: T) : MyResult<T>()
    data class Error(val error: String) : MyResult<Nothing>()
    data class ErrorException(val error: Exception) : MyResult<Nothing>()
    object Loading : MyResult<Nothing>()
}
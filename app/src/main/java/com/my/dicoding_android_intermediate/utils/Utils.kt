package com.my.dicoding_android_intermediate.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Utils {
    companion object {
        const val TOKEN = "token"
        const val DETAIL = "detail"
        const val DATABASE_NAME = "theStory"
        const val STORY_REMOTE_KEY = "theStoryRemoteKey"
    }

    fun setLocalFormat(timeStamp: ZonedDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss")
        val formattedDate = timeStamp.format(formatter)

        return formattedDate
    }
}
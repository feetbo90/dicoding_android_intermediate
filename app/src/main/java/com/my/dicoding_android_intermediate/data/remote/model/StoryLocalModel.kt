package com.my.dicoding_android_intermediate.data.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponseItem
import com.my.dicoding_android_intermediate.utils.Utils
import java.time.ZonedDateTime

@Entity(tableName = Utils.DATABASE_NAME)
data class StoryLocalModel(
    @PrimaryKey
    val id: String,
    val name: String?,
    val description: String?,
    val photoUrl: String?,
    val createdAt: String?,
    val lat: Double?,
    val lon: Double?
) {
    companion object{
        fun fromResponse(response: StoryResponseItem) = StoryLocalModel(
            id = response.id,
            name = response.name,
            description = response.description,
            photoUrl = response.photoUrl,
            createdAt = response.createdAt,
            lat = response.lat,
            lon = response.lon
        )
    }
}

fun StoryLocalModel.toDomain() = Story(
    id = id,
    name = name ?: "",
    description = description ?: "",
    photoUrl = photoUrl ?: "",
    createdAt = ZonedDateTime.parse(createdAt),
    lat = lat,
    lon = lon
)

@Entity(tableName = Utils.STORY_REMOTE_KEY)
data class StoryRemoteKey(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)

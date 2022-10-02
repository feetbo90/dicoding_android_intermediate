package com.my.dicoding_android_intermediate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keysRemote")
data class KeysRemote(
    @PrimaryKey
    val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)
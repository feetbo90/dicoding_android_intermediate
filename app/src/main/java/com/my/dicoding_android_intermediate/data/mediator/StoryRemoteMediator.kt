package com.my.dicoding_android_intermediate.data.mediator

import com.my.dicoding_android_intermediate.data.datastore.AuthDataStores
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import javax.inject.Inject

class StoryRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val dataStores: AuthDataStores,

) {
}
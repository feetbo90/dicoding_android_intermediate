package com.my.dicoding_android_intermediate.di.api

import com.my.dicoding_android_intermediate.data.remote.network.ApiConfig
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    /**
     * Provide API Service instance for Hilt
     *
     * @return ApiService
     */
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiConfig.getApiService()
}
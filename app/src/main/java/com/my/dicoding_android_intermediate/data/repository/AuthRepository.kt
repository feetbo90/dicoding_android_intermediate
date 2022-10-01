package com.my.dicoding_android_intermediate.data.repository

import com.my.dicoding_android_intermediate.data.datastore.AuthDataStores
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.remote.response.ResponseLogin
import com.my.dicoding_android_intermediate.data.remote.response.ResponseRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val authDataStores: AuthDataStores
) {
    suspend fun userLogin(email: String, password: String): Flow<Result<ResponseLogin>> = flow {
        try {
            val response = apiService.loginUser(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userRegister(email: String, fullName: String, password: String): Flow<Result<ResponseRegister>> = flow<Result<ResponseRegister>> {
        try {
            val response = apiService.registerUser(fullName, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        authDataStores.saveAuthToken(token)
    }

    fun getAuthToken(): Flow<String?> = authDataStores.getAuthToken()

}
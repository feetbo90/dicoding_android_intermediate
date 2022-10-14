package com.my.dicoding_android_intermediate.data.repository

import com.my.dicoding_android_intermediate.data.datastore.AuthDataStores
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.remote.response.ResponseLogin
import com.my.dicoding_android_intermediate.data.remote.response.ResponseRegister
import com.my.dicoding_android_intermediate.data.result.MyResult
import com.my.dicoding_android_intermediate.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val authDataStores: AuthDataStores
) {
    suspend fun userLogin(email: String, password: String): Flow<MyResult<ResponseLogin>> = flow {
        wrapEspressoIdlingResource {
            try {
                val response = apiService.loginUser(email, password)
                emit(MyResult.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(MyResult.ErrorException(Exception("Login Failed")))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun userRegister(
        email: String,
        fullName: String,
        password: String
    ): Flow<MyResult<ResponseRegister>> = flow {
        wrapEspressoIdlingResource {
            try {
                val response = apiService.registerUser(fullName, email, password)
                emit(MyResult.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(MyResult.ErrorException(Exception("Register Failed")))
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        authDataStores.saveAuthToken(token)
    }

    fun getAuthToken(): Flow<String?> = authDataStores.getAuthToken()

}
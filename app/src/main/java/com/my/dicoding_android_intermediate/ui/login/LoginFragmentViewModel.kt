package com.my.dicoding_android_intermediate.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.dicoding_android_intermediate.data.remote.response.ResponseLogin
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import com.my.dicoding_android_intermediate.data.result.MyResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginFragmentViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel(){

    suspend fun loginUser(username: String, password: String): Flow<MyResult<ResponseLogin>> =
        authRepository.userLogin(username, password)

    fun saveToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }

    fun getAuthToken(): Flow<String?> = authRepository.getAuthToken()

}
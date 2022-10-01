package com.my.dicoding_android_intermediate.ui.register

import androidx.lifecycle.ViewModel
import com.my.dicoding_android_intermediate.data.remote.response.ResponseRegister
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RegisterFragmentViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel(){

    suspend fun registerUser(fullName: String, email: String, password: String): Flow<Result<ResponseRegister>> =
        authRepository.userRegister(email, fullName, password)
}
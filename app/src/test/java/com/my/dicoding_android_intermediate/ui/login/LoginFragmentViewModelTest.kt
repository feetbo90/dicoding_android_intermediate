package com.my.dicoding_android_intermediate.ui.login

import com.my.dicoding_android_intermediate.DataDummy
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginFragmentViewModelTest {

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginFragmentViewModel: LoginFragmentViewModel
    private val dummyLogin = DataDummy.generateDummyLogin()

    @Before
    fun setUp() {
        loginFragmentViewModel = LoginFragmentViewModel(authRepository)
    }

    @Test
    fun loginUser() {
    }

    @Test
    fun saveToken() {
    }

    @Test
    fun getAuthToken() {
    }
}
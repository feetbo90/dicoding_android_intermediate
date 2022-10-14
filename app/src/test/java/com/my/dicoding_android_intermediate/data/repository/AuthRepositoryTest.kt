package com.my.dicoding_android_intermediate.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.my.dicoding_android_intermediate.DataDummy
import com.my.dicoding_android_intermediate.MainDispatcherRule
import com.my.dicoding_android_intermediate.data.datastore.AuthDataStores
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.data.result.MyResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var authDataStores: AuthDataStores

    private lateinit var authRepository: AuthRepository
    private val dummyLogin = DataDummy.generateDummyLogin()
    private val dummyRegister = DataDummy.generateDummyRegister()
    private val errorLogin = DataDummy.generateFailedLogin()
    private val errorRegister = DataDummy.generateFailedRegister()
    private val authToken = DataDummy.getAuthToken()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        authRepository = AuthRepository(apiService, authDataStores)
    }

    @Test
    fun userLoginSuccess() = runTest {
        `when`(apiService.loginUser("yo2@gmail.com", "123456")).thenReturn(dummyLogin)
        authRepository.userLogin("yo2@gmail.com", "123456").collect { result ->
            when (result) {
                is MyResult.Success -> {
                    assertNotNull(result)
                    assertEquals(dummyLogin, result.data)
                }
                else -> {}
            }
        }
        verify(apiService).loginUser("yo2@gmail.com", "123456")
    }

    @Test
    fun userLoginFailed() = runTest {
        `when`(apiService.loginUser("bla@gmail.com", "123456")).then { errorLogin }
        authRepository.userLogin("bla@gmail.com", "123456").collect { result ->
            assertNotNull(result)
            when(result) {
                is MyResult.ErrorException -> {
                    assertTrue(true)
                }
                is MyResult.Success -> {
                    assertTrue(false)
                }
                is MyResult.Error -> {
                    assertTrue(true)
                }
                else -> {}
            }
        }
        verify(apiService).loginUser("bla@gmail.com", "123456")
    }

    @Test
    fun userRegisterSuccess() = runTest {
        `when`(apiService.registerUser("Mhd Iqbal Pradipta", "yo2@gmail.com", "123456")).thenReturn(
            dummyRegister
        )
        authRepository.userRegister("yo2@gmail.com", "Mhd Iqbal Pradipta", "123456")
            .collect { result ->
                when (result) {
                    is MyResult.Success -> {
                        assertNotNull(result)
                        assertEquals(result.data, dummyRegister)
                    }
                    else -> {}
                }
            }
        verify(apiService).registerUser("Mhd Iqbal Pradipta", "yo2@gmail.com", "123456")
    }

    @Test
    fun userRegisterFailed() = runTest {
        `when`(apiService.registerUser("Blaser", "bla@gmail.com", "123456")).then { errorRegister}
        authRepository.userRegister("bla@gmail.com", "Blaser", "123456").collect { result ->
            when (result) {
                is MyResult.ErrorException -> {
                    assertTrue(true)
                }
                is MyResult.Success -> {
                    assertTrue(false)
                }
                is MyResult.Error -> {
                    assertTrue(true)
                }
                else -> {}
            }
        }
        verify(apiService).registerUser("Blaser", "bla@gmail.com", "123456")
    }

    @Test
    fun saveAuthToken() = runTest {
        authRepository.saveAuthToken("auth_token")
        verify(authDataStores).saveAuthToken("auth_token")
    }

    @Test
    fun getAuthToken() = runTest {
        `when`(authDataStores.getAuthToken()).thenReturn(authToken)
        authRepository.getAuthToken().collect { result ->
            assertNotNull(result)
            assertEquals(result, "auth_token")
        }
        verify(authDataStores).getAuthToken()
    }
}
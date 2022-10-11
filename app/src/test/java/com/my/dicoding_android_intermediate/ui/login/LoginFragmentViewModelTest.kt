package com.my.dicoding_android_intermediate.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.my.dicoding_android_intermediate.DataDummy
import com.my.dicoding_android_intermediate.MainDispatcherRule
import com.my.dicoding_android_intermediate.data.remote.response.ResponseLogin
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import com.my.dicoding_android_intermediate.data.result.MyResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginFragmentViewModel: LoginFragmentViewModel
    private val dummyLogin = DataDummy.generateDummyLogin()
    private val errorLogin = DataDummy.generateFailedLogin()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        loginFragmentViewModel = LoginFragmentViewModel(authRepository)
    }

    @Test
    fun `login User should not null and return success`() = runTest {
        val expectedLogin = flow {
            emit(MyResult.Success(dummyLogin))
        }
        `when`(authRepository.userLogin("yo2@gmail.com", "123456")).thenReturn(expectedLogin)
        loginFragmentViewModel.loginUser("yo2@gmail.com", "123456").collect { result ->
            when (result) {
                is MyResult.Success -> {
                    assertNotNull(result)
                    assertEquals(dummyLogin, result.data)
                }
                else -> {}
            }
        }
        Mockito.verify(authRepository).userLogin("yo2@gmail.com", "123456")
    }

    @Test
    fun `login User should return failed`() = runTest {
        val expectedLogin : Flow<MyResult.ErrorException> = flow {
            emit(errorLogin)
        }
        `when`(authRepository.userLogin("bla@gmail.com", "123456")).thenReturn(expectedLogin)
        loginFragmentViewModel.loginUser("bla@gmail.com", "123456").collect { result ->
            when (result) {
                is MyResult.ErrorException -> {
                    assertNotNull(result)
                    assertEquals(errorLogin , result)
                }
                is MyResult.Success -> {
                    assertTrue(false)
                }
                else -> {}
            }
        }
        Mockito.verify(authRepository).userLogin("bla@gmail.com", "123456")
    }

    @Test
    fun saveToken() = runTest{
        loginFragmentViewModel.saveToken("auth_token")
        Mockito.verify(authRepository).saveAuthToken("auth_token")
    }

    @Test
    fun getAuthToken() = runTest {
        loginFragmentViewModel.getAuthToken()
        Mockito.verify(authRepository).getAuthToken()
    }
}
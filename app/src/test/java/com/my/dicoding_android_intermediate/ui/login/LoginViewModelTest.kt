package com.my.dicoding_android_intermediate.ui.login

import com.my.dicoding_android_intermediate.data.remote.response.ResponseLogin
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import com.my.dicoding_android_intermediate.test.DataTest
import com.my.dicoding_android_intermediate.utils.CoroutinesTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTest()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var loginViewModel: LoginFragmentViewModel

    private val dummyLoginResponse = DataTest.generateDummyLoginResponse()
    private val dummyToken = "authentication_token"
    private val dummyEmail = "email@mail.com"
    private val dummyPassword = "password"

    @Before
    fun setup() {
        loginViewModel = LoginFragmentViewModel(authRepository)
    }

    @Test
    fun `login successfully - result success`(): Unit = runTest {
        val expectedResponse = flow {
            emit(Result.success(dummyLoginResponse))
        }

        Mockito.`when`(loginViewModel.loginUser(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.loginUser(dummyEmail, dummyPassword).collect { result ->

            Assert.assertTrue(result.isSuccess)
            Assert.assertFalse(result.isFailure)

            result.onSuccess { actualResponse ->
                Assert.assertNotNull(actualResponse)
                Assert.assertSame(dummyLoginResponse, actualResponse)
            }
        }

        Mockito.verify(authRepository).userLogin(dummyEmail, dummyPassword)
    }

    @Test
    fun `login failed - result failure with exception`(): Unit = runTest {
        val expectedResponse: Flow<Result<ResponseLogin>> =
            flowOf(Result.failure(Exception("login failed")))

        Mockito.`when`(loginViewModel.loginUser(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        loginViewModel.loginUser(dummyEmail, dummyPassword).collect { result ->

            Assert.assertFalse(result.isSuccess)
            Assert.assertTrue(result.isFailure)

            result.onFailure {
                Assert.assertNotNull(it)
            }
        }

        Mockito.verify(authRepository).userLogin(dummyEmail, dummyPassword)
    }

    @Test
    fun `Save authentication token successfully`(): Unit = runTest {
        loginViewModel.saveToken(dummyToken)
        Mockito.verify(authRepository).saveAuthToken(dummyToken)
    }
}
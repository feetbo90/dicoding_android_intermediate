package com.my.dicoding_android_intermediate.data.repository

import com.my.dicoding_android_intermediate.data.datastore.AuthDataStores
import com.my.dicoding_android_intermediate.data.remote.network.ApiService
import com.my.dicoding_android_intermediate.test.DataTest
import com.my.dicoding_android_intermediate.utils.CoroutinesTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class AuthRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTest()

    @Mock
    private lateinit var preferencesDataSource: AuthDataStores

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var authRepository: AuthRepository

    private val dummyName = "yoo"
    private val dummyEmail = "yoomail@mail.com"
    private val dummyPassword = "yoo_password"
    private val dummyToken = "yoo_authentication_token"

    @Before
    fun setup() {
        authRepository = AuthRepository(apiService, preferencesDataSource)
    }

    @Test
    fun `login user successfully`(): Unit = runTest {
        val expectedResponse = DataTest.generateDummyLoginResponse()

        Mockito.`when`(apiService.loginUser(dummyEmail, dummyPassword)).thenReturn(expectedResponse)

        authRepository.userLogin(dummyEmail, dummyPassword).collect { result ->
            Assert.assertTrue(result.isSuccess)
            Assert.assertFalse(result.isFailure)

            result.onSuccess { actualResponse ->
                Assert.assertNotNull(actualResponse)
                Assert.assertEquals(expectedResponse, actualResponse)
            }

            result.onFailure {
                Assert.assertNull(it)
            }
        }

    }

    @Test
    fun `login user failed - throw exception`(): Unit = runTest {
        Mockito.`when`(apiService.loginUser(dummyEmail, dummyPassword)).then { throw Exception() }

        authRepository.userLogin(dummyEmail, dummyPassword).collect { result ->
            Assert.assertFalse(result.isSuccess)
            Assert.assertTrue(result.isFailure)

            result.onFailure {
                Assert.assertNotNull(it)
            }
        }
    }


    @Test
    fun `save auth token successfully`() = runTest {
        authRepository.saveAuthToken(dummyToken)
        Mockito.verify(preferencesDataSource).saveAuthToken(dummyToken)
    }

    @Test
    fun `get authentication token successfully`() = runTest {
        val expectedToken = flowOf(dummyToken)

        Mockito.`when`(preferencesDataSource.getAuthToken()).thenReturn(expectedToken)

        authRepository.getAuthToken().collect { actualToken ->
            Assert.assertNotNull(actualToken)
            Assert.assertEquals(dummyToken, actualToken)
        }

        Mockito.verify(preferencesDataSource).getAuthToken()
    }

}
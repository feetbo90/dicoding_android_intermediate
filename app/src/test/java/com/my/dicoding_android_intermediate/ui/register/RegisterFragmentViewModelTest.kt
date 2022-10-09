package com.my.dicoding_android_intermediate.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.my.dicoding_android_intermediate.DataDummy
import com.my.dicoding_android_intermediate.MainDispatcherRule
import com.my.dicoding_android_intermediate.data.remote.response.ResponseLogin
import com.my.dicoding_android_intermediate.data.remote.response.ResponseRegister
import com.my.dicoding_android_intermediate.data.repository.AuthRepository
import com.my.dicoding_android_intermediate.ui.login.LoginFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: AuthRepository
    private lateinit var registerFragmentViewModel: RegisterFragmentViewModel
    private val dummyRegister = DataDummy.generateDummyRegister()
    private val errorRegister = DataDummy.generateFailedRegister()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        registerFragmentViewModel = RegisterFragmentViewModel(authRepository)
    }

    @Test
    fun`register User should not null and return success`() = runTest {
        val expectedRegister = flow {
            emit(Result.success(dummyRegister))
        }
        `when`(authRepository.userRegister("yo2@gmail.com", "Mhd Iqbal Pradipta", "123456")).thenReturn(expectedRegister)
        registerFragmentViewModel.registerUser("Mhd Iqbal Pradipta", "yo2@gmail.com", "123456").collect { result ->
            Assert.assertNotNull(result)
            Assert.assertTrue(result.isSuccess)
            result.onSuccess {
                Assert.assertEquals(it, dummyRegister)
            }
        }
        Mockito.verify(authRepository).userRegister("yo2@gmail.com", "Mhd Iqbal Pradipta","123456")
    }

    @Test
    fun `register User should return failed`() = runTest {
        val expectedRegister : Flow<Result<ResponseRegister>> = flow {
            emit(Result.failure(errorRegister))
        }
        `when`(authRepository.userRegister("yo2@gmail.com", "Mhd Iqbal Pradipta", "123456")).thenReturn(expectedRegister)
        registerFragmentViewModel.registerUser("Mhd Iqbal Pradipta", "yo2@gmail.com", "123456").collect { result ->
            Assert.assertNotNull(result)
            Assert.assertTrue(result.isFailure)
            result.onFailure {
                Assert.assertEquals(it, errorRegister)
            }
        }
        Mockito.verify(authRepository).userRegister("yo2@gmail.com", "Mhd Iqbal Pradipta", "123456")
    }


}
package com.my.dicoding_android_intermediate.ui.login

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.my.dicoding_android_intermediate.ConfigRunnerTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
class LoginFragmentTest {

    @Test
    fun getHeadlineNews_Success() {
        launchFragmentInContainer<LoginFragment>()
    }
}
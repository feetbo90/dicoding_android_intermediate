package com.my.dicoding_android_intermediate.ui.login

import com.my.dicoding_android_intermediate.launchFragmentInHiltContainer
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun getHeadlineNews_Success() {
        launchFragmentInHiltContainer<LoginFragment>()
    }
}
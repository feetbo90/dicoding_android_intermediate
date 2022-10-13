package com.my.dicoding_android_intermediate.ui.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.my.dicoding_android_intermediate.BuildConfig
import com.my.dicoding_android_intermediate.converter.JsonConverter
import com.my.dicoding_android_intermediate.data.remote.network.ApiConfig
import com.my.dicoding_android_intermediate.launchFragmentInHiltContainer
import dagger.hilt.android.testing.CustomTestApplication
import com.my.dicoding_android_intermediate.R

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.API_BASE_URL = "http://127.0.0.1:8080/"

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getHeadlineNews_Success() {
        launchFragmentInHiltContainer<LoginFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("login_success.json"))
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.username)).check(matches(isDisplayed()))
        onView(withText("Login Success")).check(matches(isDisplayed()))
    }
}
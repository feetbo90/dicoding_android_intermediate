package com.my.dicoding_android_intermediate.ui.register

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.converter.JsonConverter
import com.my.dicoding_android_intermediate.data.remote.network.ApiConfig
import com.my.dicoding_android_intermediate.launchFragmentInHiltContainer
import com.my.dicoding_android_intermediate.utils.EspressoIdlingResources
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RegisterFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(9000)
        ApiConfig.API_BASE_URL = "http://127.0.0.1:9000/"
        IdlingRegistry.getInstance().register(EspressoIdlingResources.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.countingIdlingResource)
    }

    @Test
    fun getRegisterSuccess() {
        launchFragmentInHiltContainer<RegisterFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("register_success.json"))
        mockWebServer.enqueue(mockResponse)
        Espresso.onView(ViewMatchers.withId(R.id.fullName))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.view_loading))
            .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
    }

}
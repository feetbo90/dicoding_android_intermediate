package com.my.dicoding_android_intermediate.ui.home

import androidx.paging.ExperimentalPagingApi
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.Espresso.onView
import androidx.test.filters.MediumTest
import com.my.dicoding_android_intermediate.data.remote.network.ApiConfig.Companion.API_URL_MOCK
import com.my.dicoding_android_intermediate.utils.ConverterJson
import com.my.dicoding_android_intermediate.utils.EspressoIdling
import com.my.dicoding_android_intermediate.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import com.my.dicoding_android_intermediate.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@ExperimentalPagingApi
@HiltAndroidTest
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        API_URL_MOCK = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdling.countingIdlingResource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdling.countingIdlingResource)
    }

    @Test
    fun launchHomeFragmentSuccess() {
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(ConverterJson.readStringFromFile("response_success.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.toolbar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.stories))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun launchHomeFragmentEmpty() {
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(ConverterJson.readStringFromFile("response_unsuccess.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.iv_not_found_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.status))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun launchHomeFragmentFailed() {
        launchFragmentInHiltContainer<HomeFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.iv_not_found_error))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.status))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}
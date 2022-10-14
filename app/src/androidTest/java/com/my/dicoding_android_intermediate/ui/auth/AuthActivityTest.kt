package com.my.dicoding_android_intermediate.ui.auth

import android.service.autofill.TextValueSanitizer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.my.dicoding_android_intermediate.utils.EspressoIdlingResources
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.my.dicoding_android_intermediate.R

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class AuthActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activity = ActivityScenarioRule(AuthActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.countingIdlingResource)
    }

    @Test
    fun showDetailSuccess() {
        onView(withId(R.id.username)).perform(typeText("yo2@gmail.com"), click())
    }
}
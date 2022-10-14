package com.my.dicoding_android_intermediate.ui.auth

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.my.dicoding_android_intermediate.utils.EspressoIdlingResources
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.ui.create.CreateStoryActivity
import com.my.dicoding_android_intermediate.ui.detail.DetailActivity
import com.my.dicoding_android_intermediate.ui.main.MainActivity

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
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResources.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResources.countingIdlingResource)
    }

    @Test
    fun loadStorySuccess() {
        onView(withId(R.id.stories)).check(matches(isDisplayed()))
        onView(withId(R.id.stories)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
    }

    @Test
    fun showDetailSuccess() {
        Intents.init()
        onView(withId(R.id.stories)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        intended(hasComponent(DetailActivity::class.java.name))
        onView(withId(R.id.tv_story_username)).check(matches(isDisplayed()))
    }

    @Test
    fun loadStoryToDetailAndGoToCreateStory() {
        Intents.init()
        onView(withId(R.id.stories)).check(matches(isDisplayed()))
        onView(withId(R.id.stories)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(9))

        onView(withId(R.id.stories)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, click()))
        intended(hasComponent(DetailActivity::class.java.name))
        onView(withId(R.id.tv_story_username)).check(matches(isDisplayed()))
        pressBack()
        onView(withId(R.id.fab_create_story)).perform(click())
        intended(hasComponent(CreateStoryActivity::class.java.name))
        pressBack()
    }
}
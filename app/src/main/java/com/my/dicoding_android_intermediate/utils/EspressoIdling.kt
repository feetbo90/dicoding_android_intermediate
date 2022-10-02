package com.my.dicoding_android_intermediate.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdling {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    EspressoIdling.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdling.decrement() // Set app as idle.
    }
}
package com.example.myapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{
    private val dummyVolume = "504.0"
    private val dummyCirumference = "100.0"
    private val dummySurfaceArea = "396.0"
    private val dummyLength = "12.0"
    private val dummyWidth = "7.0"
    private val dummyHeight = "6.0"
    private val emptyInput = ""
    private val fieldEmpty = "Field ini tidak blh kosong"

//    @get:Rule
//    var mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun assertGetCircumference() {
        Espresso.onView(withId(R.id.edt_length))
            .perform(ViewActions.typeText(dummyLength), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.edt_width))
            .perform(ViewActions.typeText(dummyWidth), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.edt_height))
            .perform(ViewActions.typeText(dummyHeight), ViewActions.closeSoftKeyboard())
        Espresso.onView(withId(R.id.btn_save))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.btn_save)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.btn_calculate_cirumference))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.btn_calculate_cirumference)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.tv_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.tv_result))
            .check(ViewAssertions.matches(ViewMatchers.withText(dummyCirumference)))
    }
}
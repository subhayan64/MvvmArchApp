package com.example.mvvmarchapp.view

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import com.example.mvvmarchapp.R
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @Rule
    val activityScenarioRule = activityScenarioRule<MainActivity>()

    @Test
    fun `test_bottomNavigationButton_expectedGridFragment`() {
        onView(withId(R.id.svSearchView)).check(matches((isDisplayed())))
        onView(withId(R.id.gridViewFragment)).perform(click())
        onView(withId(R.id.gridViewFragment)).check(matches(isCompletelyDisplayed()))
    }

}
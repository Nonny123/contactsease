package com.decagon.android.sq007

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.decagon.android.sq007.R
import com.decagon.android.sq007.ui.MainActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    //Test if MainActivity is Visible
    @Test
    fun testActivityInView() {
        onView(ViewMatchers.withId(R.id.activity_main)).check(matches(isDisplayed()))
    }

    //Test Recycler View Visibility
    @Test
    fun testRecylerviewInView() {
        onView(ViewMatchers.withId(R.id.recycler_view_contacts)).check(matches(isDisplayed()))
    }

//    @Test
//    fun testRecyclerClick(){
//        Espresso.onView(ViewMatchers.withId(R.id.recycler_view_contacts))
//            .inRoot(RootMatchers.withDecorView(
//                Matchers.is(activityRule.scenario.g)
//            ))
//    }

//    @Test
//    fun testRecyclerClick() {
//        onView(withId(R.id.recycler_view_contacts)).perform(RecyclerViewActions.actionOnHolderItem((2, click()));
//    }
}
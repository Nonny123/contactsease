package com.decagon.android.sq007

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.decagon.android.sq007.R
import com.decagon.android.sq007.ui.ContactDetailsActivity
import com.decagon.android.sq007.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ContactDetailsActivityTest {
    @get:Rule
    val activityRule: ActivityScenarioRule<ContactDetailsActivity> =
        ActivityScenarioRule(ContactDetailsActivity::class.java)

    //Test if ContactDetailsActivity is Visible
    @Test
    fun testActivityInView() {
        onView(ViewMatchers.withId(R.id.contact_details)).check(matches(isDisplayed()))
    }

    //Test Nested Scroll View Visibility
    @Test
    fun testNestedScrollInView() {
        onView(ViewMatchers.withId(R.id.nestedScrollView)).check(matches(isDisplayed()))
    }

}
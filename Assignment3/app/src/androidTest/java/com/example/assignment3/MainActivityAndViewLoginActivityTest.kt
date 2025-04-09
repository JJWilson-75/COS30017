package com.example.assignment3


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityAndViewLoginActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityAndViewLoginActivityTest() {
        //Part 1 Check some item name and user name when MainActivity starts
        onView(allOf(withId(R.id.item_name_text_view), withText("Google"))).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.username_text_view), withText("john.doe92"))).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.item_name_text_view), withText("Twitter"))).check(matches(isDisplayed()))

        onView(allOf(withId(R.id.username_text_view), withText("johnny_doe22"))).check(matches(isDisplayed()))

        //Special usage to select the button in item_login with the item_name_text_view Facebook
        onView(allOf(
            withId(R.id.view_button),
            isDescendantOfA(withItemContainingText(R.id.item_name_text_view, "Facebook"))
        )).perform(click())

        //Part 2 Check the details when click View
        onView(withId(R.id.item_name_text_view)).check(matches(withText("Facebook")))

        onView(withId(R.id.username_text_view)).check(matches(withText("jane_doe_88")))

        onView(withId(R.id.password_text_view)).check(matches(withText("F@ceB00k$88")))

        onView(withId(R.id.website_url_text_view)).check(matches(withText("https://facebook.com")))

        onView(withId(R.id.note_text_view)).check(matches(withText("Social media account")))
    }

    private fun withItemContainingText(textViewId: Int, text: String): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("is an item view containing a TextView with text: $text")
            }

            override fun matchesSafely(item: View?): Boolean {
                if (item == null) return false
                val textView = item.findViewById<View>(textViewId) as? TextView
                return textView != null && textView.text.toString() == text
            }
        }
    }
}

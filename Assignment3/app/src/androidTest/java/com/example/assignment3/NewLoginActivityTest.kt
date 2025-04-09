package com.example.assignment3


import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.contrib.RecyclerViewActions

@LargeTest
@RunWith(AndroidJUnit4::class)
class NewLoginActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun newLoginActivityTest() {
        // Click "New" button to start creating a new login
        onView(withId(R.id.new_button)).perform(click())

        // Enter details in the form
        onView(withId(R.id.item_name_edit_text)).perform(replaceText("Fanfiction"), closeSoftKeyboard())
        onView(withId(R.id.item_name_edit_text)).perform(pressImeActionButton())

        onView(withId(R.id.username_edit_text)).perform(replaceText("JohnCal1"), closeSoftKeyboard())
        onView(withId(R.id.username_edit_text)).perform(pressImeActionButton())

        onView(withId(R.id.password_edit_text)).perform(replaceText("abc123456!"), closeSoftKeyboard())
        onView(withId(R.id.password_edit_text)).perform(pressImeActionButton())

        onView(withId(R.id.website_url_edit_text)).perform(replaceText("fanfiction.net"), closeSoftKeyboard())
        onView(withId(R.id.website_url_edit_text)).perform(pressImeActionButton())

        onView(withId(R.id.note_edit_text)).perform(replaceText("Post fictional stories"), closeSoftKeyboard())

        // Save the new login
        onView(withId(R.id.save_button)).perform(click())

        // Scroll to the "Fanfiction" item in the RecyclerView
        onView(withId(R.id.recycler_view_logins)).perform(
            RecyclerViewActions.scrollTo<androidx.recyclerview.widget.RecyclerView.ViewHolder>(
                hasDescendant(allOf(withId(R.id.item_name_text_view), withText("Fanfiction")))
            )
        )

        //Special usage to select the button in item_login with the item_name_text_view Facebook
        onView(allOf(
            withId(R.id.view_button),
            isDescendantOfA(withItemContainingText(R.id.item_name_text_view, "Fanfiction"))
        )).perform(click())

        onView(withId(R.id.item_name_text_view)).check(matches(withText("Fanfiction")))

        onView(withId(R.id.username_text_view)).check(matches(withText("JohnCal1")))

        onView(withId(R.id.password_text_view)).check(matches(withText("abc123456!")))

        onView(withId(R.id.website_url_text_view)).check(matches(withText("fanfiction.net")))

        onView(withId(R.id.note_text_view)).check(matches(withText("Post fictional stories")))
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

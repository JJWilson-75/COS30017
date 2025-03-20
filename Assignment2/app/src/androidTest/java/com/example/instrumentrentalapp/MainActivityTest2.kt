package com.example.instrumentrentalapp


import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.slider.Slider
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest2 {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest2() {
        //Select an instrument to borrow, then check if the details in MainActivity matches
        repeat(2) {
            onView(allOf(withId(R.id.btnNext))).perform(click())
        }

        onView(allOf(withId(R.id.creditAccount))).check(matches(withText("4000.0")))

        onView(allOf(withId(R.id.btnInstrumentBorrow))).check(matches(not(isEnabled())))

        onView(allOf(withId(R.id.btnBorrow))).perform(click())

        onView(allOf(withId(R.id.rentalEmail))).perform(replaceText("johnjames1@gmail.com"), closeSoftKeyboard())

        onView(allOf(withId(R.id.rentalEmail))).perform(pressImeActionButton())

        onView(allOf(withId(R.id.instrumentName))).check(matches(withText("Steinway & Sons Model D Concert Grand")))

        onView(allOf(withId(R.id.rentalEmail))).check(matches(withText("johnjames1@gmail.com")))

        onView(withId(R.id.rentalNumber)).perform(setValue(2.0F))

        onView(withId(R.id.rentalNumber)).check(matches(withValue(2.0F)))

        onView(allOf(withId(R.id.labelRentalNumber))).check(matches(withText("Number: 2")))

        onView(withId(R.id.rentalTime)).perform(setValue(4.0F))

        onView(withId(R.id.rentalTime)).check(matches(withValue(4.0F)))

        onView(allOf(withId(R.id.labelRentalTime))).check(matches(withText("Rental Time (Month): 4")))

        onView(allOf(withId(R.id.rentalPrice))).check(matches(withText("Total Price $1364.0")))

        onView(allOf(withId(R.id.btnConfirm))).perform(click())

        onView(allOf(withId(R.id.instrumentName))).check(matches(withText("Steinway & Sons Model D Concert Grand")))

        onView(allOf(withId(R.id.creditAccount))).check(matches(withText("2636.0")))

        onView(allOf(withId(R.id.btnInstrumentBorrow))).check(matches(isEnabled()))

        onView(allOf(withId(R.id.btnInstrumentBorrow))).perform(click())

        onView(allOf(withId(android.R.id.message))).check(matches(withText("+ Rental ID: 1\n+ Instrument Name: Steinway & Sons Model D Concert Grand\n+ Email: johnjames1@gmail.com\n+ Number: 2\n+ Rental Time (Month): 4\n+ Total Price: $1364.0")))
    }

    fun withValue(expectedValue: Float): Matcher<View?> {
        return object : BoundedMatcher<View?, Slider>(Slider::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("expected: $expectedValue")
            }

            override fun matchesSafely(slider: Slider?): Boolean {
                return slider?.value == expectedValue
            }
        }
    }

    fun setValue(value: Float): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Slider value to $value"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(Slider::class.java)
            }

            override fun perform(uiController: UiController?, view: View) {
                val seekBar = view as Slider
                seekBar.value = value
            }
        }
    }
}

package com.example.instrumentrentalapp


import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.material.slider.Slider
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import androidx.test.espresso.matcher.BoundedMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BookingActivityTest1 {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun bookingActivityTest1() {
        //Part 1, first unselect the Saxophone chip, click the Prev button twice, then the Borrow button to transition into Booking Activity
        onView(allOf(withId(R.id.chipSaxophone))).perform(click())

        repeat(2) {
            onView(allOf(withId(R.id.btnPrev))).perform(click())
        }

        onView(allOf(withId(R.id.btnBorrow))).perform(click())

        onView(allOf(withId(R.id.instrumentName))).check(matches(withText("Yamaha FG800 Acoustic")))

        onView(allOf(withId(R.id.labelRentalNumber))).check(matches(withText("Number: 1")))

        onView(allOf(withId(R.id.labelRentalTime))).check(matches(withText("Rental Time (Month): 1")))

        onView(allOf(withId(R.id.rentalPrice))).check(matches(withText("Total Price $220.75")))

        onView(allOf(withId(R.id.creditAccount))).check(matches(withText("4000.0")))

        onView(allOf(withId(R.id.appName))).check(matches(withText("Instrument Rental")))

        onView(allOf(withId(R.id.btnConfirm))).check(matches(withText("Confirm")))

        onView(allOf(withId(R.id.btnCancel))).check(matches(withText("Cancel")))
        //End of part 1

        //Part 2, change the language
        onView(allOf(withId(R.id.btnChangeLang))).perform(click())

        onView(allOf(withId(R.id.creditAccount))).check(matches(withText("4000.0")))

        onView(allOf(withId(R.id.appName))).check(matches(withText("Thuê Nhạc Cụ")))

        onView(allOf(withId(R.id.instrumentName))).check(matches(withText("Yamaha FG800 Acoustic")))

        onView(allOf(withId(R.id.labelRentalNumber))).check(matches(withText("Số Lượng: 1")))

        onView(allOf(withId(R.id.labelRentalTime))).check(matches(withText("Thời Gian Thuê (Tháng): 1")))

        onView(allOf(withId(R.id.rentalPrice))).check(matches(withText("Tổng Tiền $220.75")))

        onView(allOf(withId(R.id.btnConfirm))).check(matches(withText("Xác Nhận")))

        onView(allOf(withId(R.id.btnCancel))).check(matches(withText("Hủy")))
        //End of part 2

        //Part 3, type in the email, drag both sliders with customized functions
        onView(allOf(withId(R.id.rentalEmail))).perform(replaceText("johnjames1@gmail.com"), closeSoftKeyboard())

        onView(allOf(withId(R.id.rentalEmail))).perform(pressImeActionButton())

        onView(allOf(withId(R.id.rentalEmail))).check(matches(withText("johnjames1@gmail.com")))

        onView(withId(R.id.rentalNumber)).perform(setValue(3.0F))

        onView(withId(R.id.rentalNumber)).check(matches(withValue(3.0F)))

        onView(allOf(withId(R.id.labelRentalNumber))).check(matches(withText("Số Lượng: 3")))

        onView(withId(R.id.rentalTime)).perform(setValue(2.0F))

        onView(withId(R.id.rentalTime)).check(matches(withValue(2.0F)))

        onView(allOf(withId(R.id.labelRentalTime))).check(matches(withText("Thời Gian Thuê (Tháng): 2")))

        onView(allOf(withId(R.id.rentalPrice))).check(matches(withText("Tổng Tiền $1324.5")))
        //End of part 3
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

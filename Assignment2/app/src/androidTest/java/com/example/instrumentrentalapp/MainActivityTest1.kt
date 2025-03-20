package com.example.instrumentrentalapp


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest1 {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest1() {
        //Part 1, where the activity is first loaded
        onView(allOf(withId(R.id.instrumentName))).check(matches(withText("Fender American Professional II Stratocaster")))

        onView(allOf(withId(R.id.instrumentStockNumber))).check(matches(withText("Stock Number: 5")))

        onView(allOf(withId(R.id.instrumentType))).check(matches(withText("Type: Guitar")))

        onView(allOf(withId(R.id.instrumentPrice))).check(matches(withText("Monthly Price: $1500.25")))

        onView(allOf(withId(R.id.instrumentWeight))).check(matches(withText("Weight: 3.6kg")))

        onView(allOf(withId(R.id.creditAccount))).check(matches(withText("4000.0")))

        onView(allOf(withId(R.id.btnInstrumentBorrow))).check(matches(not(isEnabled())))
        //End of part 1

        //Part 2, unselect the Guitar chip, then click Next twice
        onView(allOf(withId(R.id.chipGuitar))).perform(click())

        repeat(2) {
            onView(allOf(withId(R.id.btnNext))).perform(click())
        }

        onView(allOf(withId(R.id.instrumentName))).check(matches(withText("Selmer Paris Series II Tenor")))

        onView(allOf(withId(R.id.instrumentType))).check(matches(withText("Type: Saxophone")))

        onView(allOf(withId(R.id.instrumentPrice))).check(matches(withText("Monthly Price: $3900.5")))

        onView(allOf(withId(R.id.instrumentStockNumber))).check(matches(withText("Stock Number: 3")))

        onView(allOf(withId(R.id.instrumentWeight))).check(matches(withText("Weight: 3.2kg")))
        //End of part 2

        //Part 3, change language
        onView(allOf(withId(R.id.btnChangeLang))).perform(click())

        onView(allOf(withId(R.id.chipGuitar))).check(matches(withText("Đàn Guitar")))

        onView(allOf(withId(R.id.chipPiano))).check(matches(withText("Dương Cầm")))

        onView(allOf(withId(R.id.chipSaxophone))).check(matches(withText("Kèn Saxophone")))

        onView(allOf(withId(R.id.instrumentType))).check(matches(withText("Thể Loại: Kèn Saxophone")))

        onView(allOf(withId(R.id.instrumentPrice))).check(matches(withText("Giá Tháng: $3900.5")))

        onView(allOf(withId(R.id.instrumentStockNumber))).check(matches(withText("Tồn Kho: 3")))

        onView(allOf(withId(R.id.instrumentWeight))).check(matches(withText("Khối Lượng: 3.2kg")))

        onView(allOf(withId(R.id.creditAccount))).check(matches(withText("4000.0")))
        //End of part 3

        //Part 4, which only the saxophone is filtered, thus disabling Next and Previous buttons
        onView(allOf(withId(R.id.chipPiano))).perform(click())

        onView(allOf(withId(R.id.btnPrev))).check(matches((not(isEnabled()))))

        onView(allOf(withId(R.id.btnNext))).check(matches((not(isEnabled()))))
        //End of part 4
    }
}

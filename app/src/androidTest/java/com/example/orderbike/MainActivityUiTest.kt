package com.example.orderbike

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.orderbike.ui.MainActivity
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith



/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.orderbike", appContext.packageName)
    }


    @Test
    fun appLaunchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun onLaunchToolBarIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun onLaunchMatchRecyclerViewIsDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.recyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun onRecylcerViewClick(){
       // onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.no_of_place)));

    }


    object MyViewAction {
        fun clickChildViewWithId(id: Int): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View>? {
                    return null
                }

                override fun getDescription(): String {
                    return "Click on a child view with specified id."
                }

                override fun perform(uiController: UiController?, view: View) {
                    val v: View = view.findViewById(id)
                    v.performClick()
                }
            }
        }
    }

}
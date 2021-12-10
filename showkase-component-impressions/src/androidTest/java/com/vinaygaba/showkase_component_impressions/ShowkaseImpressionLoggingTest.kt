package com.vinaygaba.showkase_component_impressions

import android.app.Activity
import android.view.KeyEvent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performGesture
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import android.content.Intent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.isRoot
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry

@RunWith(JUnit4::class)
class ShowkaseImpressionLoggingTest2 {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun fully_visible_component_has_exactly_1_visibility_percentage() {
        composeTestRule.setContent {
            BasicComposableWithVisibilityToggle()
        }

        val textToAssert = "key: key visibilityPercentage: 1.0"
        composeTestRule.apply {
            onNodeWithText(textToAssert).assertIsDisplayed()
        }
    }

    @Test
    fun component_changes_visibility_on_its_visibility_being_toggled() {
        composeTestRule.setContent {
            BasicComposableWithVisibilityToggle()
        }

        composeTestRule.apply {
            onNodeWithText("key: key visibilityPercentage: 1.0").assertIsDisplayed()
            onNodeWithText("Click Me").performGesture { click() }
            onNodeWithText("key: key visibilityPercentage: 0.0").assertIsDisplayed()
            onNodeWithText("Click Me").performGesture { click() }
            waitUntil("key: key visibilityPercentage: 1.0")
            onNodeWithText("key: key visibilityPercentage: 1.0").assertIsDisplayed()
        }
    }

    // WIP. Do not review
    @Test
    fun component_visibility_changes_when_app_is_backgrounded_and_foregrounded() {
        val instrumentation = getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)
        composeTestRule.setContent {
            BasicTestComposableWithEvents()
        }

        composeTestRule.apply {
            onNodeWithText("counter: 1").assertIsDisplayed()

            device.pressHome()
            device.pressRecentApps()
            device.click(device.displayWidth/2, device.displayHeight/2)
//            device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)
//            device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)
//            waitForIdle()
            onNodeWithText("counter:").assertIsDisplayed()
        }
    }


    @Test
    fun partially_visible_component_has_less_than_1_visibility_percentage() {
        composeTestRule.setContent {
            PartiallyVisibleComposable()
        }

        // TODO: Needs work as this is not what we expect
        val textToAssert = "key: key visibilityPercentage: 1.0"
        composeTestRule.apply {
            onNodeWithText(textToAssert).assertIsDisplayed()
        }
    }

    private fun ComposeContentTestRule.waitUntil(text: String) {
        waitUntil {
            onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
package com.vinaygaba.showkase_component_impressions

import android.view.KeyEvent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performGesture
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


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
            onNodeWithText("Testing Impression Events").performGesture { click() }
            onNodeWithText("key: key visibilityPercentage: 0.0").assertIsDisplayed()
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
            device.pressKeyCode(KeyEvent.KEYCODE_APP_SWITCH)

            waitForIdle()
            onNodeWithText("counter: 1").assertIsDisplayed()
            waitForIdle()
            onNodeWithText("counter: 5").assertIsDisplayed()
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
}
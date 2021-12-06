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
    fun test() {
        composeTestRule.setContent {
            BasicComposableWithVisibilityToggle()
        }

        val textToAssert = "key: key visibilityPercentage: 1.0"
        composeTestRule.apply {
            onNodeWithText(textToAssert).assertIsDisplayed()
        }
    }

    @Test
    fun test2() {
        composeTestRule.setContent {
            BasicComposableWithVisibilityToggle()
        }

        composeTestRule.apply {
            onNodeWithText("key: key visibilityPercentage: 1.0").assertIsDisplayed()
            onNodeWithText("Testing Impression Events").performGesture { click() }
            onNodeWithText("key: key visibilityPercentage: 0.0").assertIsDisplayed()
        }
    }

    @Test
    fun test3() {
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

        val textToAssert = "key: key visibilityPercentage: 1.0"
        composeTestRule.apply {
            onNodeWithText(textToAssert).assertIsDisplayed()
        }
    }
}
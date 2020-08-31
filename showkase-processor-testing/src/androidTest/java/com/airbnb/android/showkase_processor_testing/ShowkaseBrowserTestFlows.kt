package com.airbnb.android.showkase_processor_testing

import androidx.activity.ComponentActivity
import androidx.ui.test.android.AndroidComposeTestRule
import androidx.ui.test.assertCountEquals
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.onChildren
import androidx.ui.test.onNodeWithSubstring
import androidx.ui.test.onNodeWithTag
import androidx.ui.test.onNodeWithText
import androidx.ui.test.performClick
import androidx.ui.test.performGesture
import androidx.ui.test.performTextInput
import androidx.ui.test.swipeUp
import androidx.ui.test.waitForIdle

internal fun clickRowWithText(text: String) {
    onNodeWithText(text).assertIsDisplayed().performClick()
}

internal fun clickRowWithTag(text: String) {
    onNodeWithTag(text).assertIsDisplayed().performClick()
}

internal fun inputTextWithTag(tag: String, text: String) {
    onNodeWithTag(tag).performTextInput(text)
}

internal fun verifyRowsWithTextAreDisplayed(vararg textList: String) {
    textList.forEach {
        onNodeWithText(it).assertIsDisplayed()
    }
}

internal fun verifyRowsWithTextDoesNotExist(vararg textList: String) {
    textList.forEach {
        onNodeWithText(it).assertDoesNotExist()
    }
}

internal fun <T : ComponentActivity> AndroidComposeTestRule<T>.goBack() = this
    .activityRule
    .scenario
    .onActivity {
        it.onBackPressed()
    }

internal fun verifyLandingScreen() {
    verifyRowsWithTextAreDisplayed("Components", "Typography", "Colors")
}

internal fun verifyTypographyDetailScreen() {
    verifyRowsWithTextAreDisplayed("Body1", "Body2", "H1", "H2", "H3", "H4")

    onNodeWithTag("TypographyInAGroupList").performGesture {
        swipeUp()
    }

    waitForIdle()

    verifyRowsWithTextAreDisplayed("H5", "H6", "Subtitle1", "Subtitle2")
}

internal fun verifyColorsDetailScreen() {
    onNodeWithTag("ColorsInAGroupList").onChildren().assertCountEquals(4)

    verifyRowsWithTextAreDisplayed(
        "Primary", "Primary Variant", "Secondary", "Secondary Variant"
    )
}

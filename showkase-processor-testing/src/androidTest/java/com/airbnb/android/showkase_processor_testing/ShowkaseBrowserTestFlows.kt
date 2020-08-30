package com.airbnb.android.showkase_processor_testing

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
import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity

internal fun clickRowWithText(text: String) {
    onNodeWithText(text).assertIsDisplayed().performClick()
}

internal fun clickRowWithTag(text: String) {
    onNodeWithTag(text).assertIsDisplayed().performClick()
}

internal fun inputTextWithTag(tag: String, text: String) {
    onNodeWithTag(tag).performTextInput(text)
}

internal fun verifyRowsWithTextAreDisplayed(textList: List<String>) {
    textList.forEach {
        onNodeWithText(it).assertIsDisplayed()
    }
}

internal fun verifyRowsWithTextDoesNotExist(textList: List<String>) {
    textList.forEach {
        onNodeWithText(it).assertDoesNotExist()
    }
}

internal fun verifyRowsWithSubstringDisplayed(textList: List<String>) {
    textList.forEach {
        onNodeWithSubstring(it).assertIsDisplayed()
    }
}

internal fun goBack(composeTestRule: AndroidComposeTestRule<ShowkaseBrowserActivity>) {
    composeTestRule.activityRule.scenario.onActivity {
        it.onBackPressed()
    }
}

internal fun verifyLandingScreen() {
    verifyRowsWithTextAreDisplayed(listOf("Components", "Typography", "Colors"))
}

internal fun verifyTypographyDetailScreen() {
    verifyRowsWithTextAreDisplayed(listOf("Body1", "Body2", "H1", "H2", "H3", "H4"))

    onNodeWithTag("TypographyInAGroupList").performGesture {
        swipeUp()
    }

    waitForIdle()

    verifyRowsWithTextAreDisplayed(listOf("H5", "H6", "Subtitle1", "Subtitle2"))
}

internal fun verifyColorsDetailScreen() {
    onNodeWithTag("ColorsInAGroupList").onChildren().assertCountEquals(4)

    verifyRowsWithTextAreDisplayed(
        listOf(
            "Primary", "Primary Variant", "Secondary", "Secondary Variant"
        )
    )
}

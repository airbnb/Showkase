package com.airbnb.android.showkase_processor_testing

import androidx.activity.ComponentActivity
import androidx.ui.test.AndroidComposeTestRule
import androidx.ui.test.assertCountEquals
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.onChildren
import androidx.ui.test.onNodeWithTag
import androidx.ui.test.onNodeWithText
import androidx.ui.test.performClick
import androidx.ui.test.performGesture
import androidx.ui.test.performTextInput
import androidx.ui.test.swipeUp
import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.clickRowWithText(text: String) { 
    onNodeWithText(text, false, false).assertIsDisplayed().performClick()
}

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.clickRowWithTag(text: String) {
    onNodeWithTag(text).assertIsDisplayed().performClick()
}

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.inputTextWithTag(
    tag: String, 
    text: String
) {
    onNodeWithTag(tag).performTextInput(text)
}

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.verifyRowsWithTextAreDisplayed(
    vararg textList: String
) {
    textList.forEach {
        onNodeWithText(it).assertIsDisplayed()
    }
}

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.verifyRowsWithTextDoesNotExist(
    vararg textList: String
) {
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

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.verifyLandingScreen() {
    verifyRowsWithTextAreDisplayed("Components", "Typography", "Colors")
}

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.verifyTypographyDetailScreen() {
    verifyRowsWithTextAreDisplayed("Body1", "Body2", "H1", "H2", "H3", "H4")

    onNodeWithTag("TypographyInAGroupList").performGesture {
        swipeUp()
    }

    waitForIdle()

    verifyRowsWithTextAreDisplayed("H5", "H6", "Subtitle1", "Subtitle2")
}

internal fun AndroidComposeTestRule<ShowkaseBrowserActivity>.verifyColorsDetailScreen() {
    onNodeWithTag("ColorsInAGroupList").onChildren().assertCountEquals(4)

    verifyRowsWithTextAreDisplayed(
        "Primary", "Primary Variant", "Secondary", "Secondary Variant"
    )
}

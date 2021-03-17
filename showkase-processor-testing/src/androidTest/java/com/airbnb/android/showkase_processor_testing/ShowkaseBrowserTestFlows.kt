package com.airbnb.android.showkase_processor_testing

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.clickRowWithText(
    text: String
) {
    onNodeWithText(text, false, false).assertIsDisplayed().performClick()
}

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.clickRowWithTag(
    text: String
) {
    onNodeWithTag(text).assertIsDisplayed().performClick()
}

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.inputTextWithTag(
    tag: String,
    text: String
) {
    onNodeWithTag(tag).performTextInput(text)
}

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyRowsWithTextAreDisplayed(
    vararg textList: String
) {
    textList.forEach {
        onNodeWithText(it).assertIsDisplayed()
    }
}

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyRowsWithTextDoesNotExist(
    vararg textList: String
) {
    textList.forEach {
        onNodeWithText(it).assertDoesNotExist()
    }
}

internal fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.goBack() = 
    this
    .activityRule
    .scenario
    .onActivity {
        it.onBackPressed()
    }

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyLandingScreen() {
    verifyRowsWithTextAreDisplayed("Components (5)", "Typography (13)", "Colors (4)")
}

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyTypographyDetailScreen() {
    verifyRowsWithTextAreDisplayed("Body1", "Body2", "H1", "H2", "H3", "H4")

    onNodeWithTag("TypographyInAGroupList").performGesture {
        swipeUp()
    }

    waitForIdle()

    verifyRowsWithTextAreDisplayed("H5", "H6", "Subtitle1", "Subtitle2")
}

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyColorsDetailScreen() {
    onNodeWithTag("ColorsInAGroupList").onChildren().assertCountEquals(4)

    verifyRowsWithTextAreDisplayed(
        "Primary", "Primary Variant", "Secondary", "Secondary Variant"
    )
}

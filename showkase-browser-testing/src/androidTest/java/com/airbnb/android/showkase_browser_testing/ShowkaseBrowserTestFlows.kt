package com.airbnb.android.showkase_browser_testing

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.airbnb.android.showkase.ui.SemanticsUtils.LineCountKey
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
        onNodeWithText(it).assertEitherDoesNotExistOrIsNotDisplayed()
    }
}

/**
 * LazyColumn pre-fetching may precompose items in advance even if they are not displayed, and also
 * keeps two previously visible items active but not displayed
 * (https://android-review.googlesource.com/c/platform/frameworks/support/+/1686519). There is
 * not yet a built-in assertion to determine whether a node is not visible for any reason, as
 * assertIsNotDisplayed() fails if the node does not exist.
 * See https://issuetracker.google.com/issues/187188981
 */
private fun SemanticsNodeInteraction.assertEitherDoesNotExistOrIsNotDisplayed() {
    try {
        assertDoesNotExist()
    } catch (e: AssertionError) {
        // Does exist, but may not be displayed
        assertIsNotDisplayed()
    }
}

internal fun <T : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<T>, T>.goBack() = 
    this
    .activityRule
    .scenario
    .onActivity {
        it.onBackPressed()
    }

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyLandingScreen(
    components: Int,
    typography: Int,
    colors: Int,
) {
    verifyRowsWithTextAreDisplayed("Components ($components)", "Typography ($typography)", "Colors ($colors)")
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

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyLineCountIsValue(
    value: Int
) {
    onNode(SemanticsMatcher.expectValue(LineCountKey, value)).assertExists()
}

internal fun AndroidComposeTestRule<ActivityScenarioRule<ShowkaseBrowserActivity>, ShowkaseBrowserActivity>.verifyButtonWithTagIsDisplayedAndEnabled(
    tag: String
) {
    onNodeWithTag(tag)
        .assertExists("Node with tag: $tag does not exist")
        .assertIsDisplayed()
        .assertIsEnabled()
}


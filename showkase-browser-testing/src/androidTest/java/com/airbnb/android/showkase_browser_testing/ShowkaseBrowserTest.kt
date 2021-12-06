package com.airbnb.android.showkase_browser_testing

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipeDown
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
import com.airbnb.android.showkase_browser_testing.getBrowserIntent
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ShowcaseBrowserTest {
    // This test rule allows us to run tests for compose without having to do any manual setup.
    // This also creates a blank activity for you so that you can present you composable inside it.
    // If you want to use of a custom activity, you can use AndroidComposeTestRule instead.

    // Note: One last thing that we need to do in order to use the default compose rule is that we
    // need to add the blank activity that it uses to the AndroidManifest.xml. Search for
    // android.app.Activity in the AndroidManifest.xml
    @get:Rule
    val composeTestRule =
        AndroidComposeTestRule(
            activityRule = ActivityScenarioRule<ShowkaseBrowserActivity>(
                Showkase.getBrowserIntent(
                    InstrumentationRegistry.getInstrumentation().targetContext
                )
            ),
            activityProvider = { rule ->
                var activity: ShowkaseBrowserActivity? = null
                rule.scenario.onActivity { activity = it }
                activity ?: error("Activity was not set in the ActivityScenarioRule!")
            }
        )

    @Test
    fun activity_starts_and_all_the_showkase_ui_elements_are_visible_on_the_screen_and_clickable() {
        // Assert that all the categories are displayed on the screen and that they are clickable.
        composeTestRule.verifyLandingScreen()
    }

    @Test
    fun clicking_components_takes_you_to_a_screen_with_groups_of_components() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen()

            // Tap on the "Components" row
            clickRowWithText("Components (5)")

            // Verify that all the groups are displayed on the screen
            verifyRowsWithTextAreDisplayed("Group1 (2)", "Group2 (1)", "Group3 (2)")
        }
    }

    @Test
    fun clicking_colors_takes_you_to_a_screen_with_groups_of_colors() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen()

            // Tap on the "Colors" row
            clickRowWithText("Colors (4)")

            // Verify that a row for the group "Light Colors" is visible on the screen
            verifyRowsWithTextAreDisplayed("Light Colors (4)")
        }
    }

    @Test
    fun clicking_typography_takes_you_to_a_screen_with_groups_of_typography() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen()

            // Tap on the "Typography" row
            clickRowWithText("Typography (13)")

            // Verify that all typography groups are visible on the screen
            verifyRowsWithTextAreDisplayed("Material (10)", "Holo (3)")
        }
    }

    @Test
    fun opening_component_detail_screen_has_5_permutations_displayed() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen()

            // Tap on the "Components" row
            clickRowWithText("Components (5)")

            // Select "Group1"
            clickRowWithText("Group1 (2)")

            // Verify that the correct composables are displayed on the screen
            verifyRowsWithTextAreDisplayed("Test Composable1", "Test Composable2")

            // Select "Test Composable1"
            clickRowWithText("Test Composable1")

            // Verify that the default style is visible on the screen
            verifyRowsWithTextAreDisplayed("Composable1 [Default Style]")

            // Select "Test Composable1 [Default Style]"
            clickRowWithText("Test Composable1")

            // Verify that all 5 permutations for "Test Composable1" are generated
            verifyRowsWithTextAreDisplayed(
                "Composable1 [Basic Example]",
                "Composable1 [RTL]",
                "Composable1 [Font Scaled x 2]",
                "Composable1 [Display Scaled x 2]",
                "Composable1 [Dark Mode]"
            )

            // Go back to the component styles screen
            goBack()

            waitForIdle()

            // Go back to the components in a group screen
            goBack()

            waitForIdle()

            // Select "Test Composable2"
            clickRowWithText("Test Composable2")

            // Verify that the default style is visible on the screen
            verifyRowsWithTextAreDisplayed("Composable2 [Default Style]")

            // Select "Test Composable2"
            clickRowWithText("Test Composable2")

            // Verify that all 5 permutations for "Test Composable2" are generated
            verifyRowsWithTextAreDisplayed(
                "Composable2 [Basic Example]",
                "Composable2 [RTL]",
                "Composable2 [Font Scaled x 2]",
                "Composable2 [Display Scaled x 2]",
                "Composable2 [Dark Mode]"
            )
        }
    }

    @Test
    fun selecting_color_group_has_colors_displayed() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen()

            // Tap on the "Colors" row
            clickRowWithText("Colors (4)")

            // Select group "Light Colors"
            clickRowWithText("Light Colors (4)")

            // Verify that the correct colors are displayed on the screen
            verifyColorsDetailScreen()
        }
    }

    @Test
    fun selecting_typography_group_has_colors_displayed() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen()

            // Tap on the "Typography" row
            clickRowWithText("Typography (13)")

            // Select group "Material"
            clickRowWithText("Material (10)")

            // Verify that the correct typography styles are displayed on the screen
            verifyTypographyDetailScreen()

            // Go back to the typography in a group screen
            composeTestRule.goBack()

            // Select the other group on the screen
            clickRowWithText("Holo (3)")

            // Verify that the correct typography styles are displayed on the screen
            verifyRowsWithTextAreDisplayed("Button", "Caption", "Overline")
        }
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_groups_of_components() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select Components
            clickRowWithText("Components (5)")

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            // Enter "Group1" in the search field
            inputTextWithTag("SearchTextField", "Group1 (2)")

            // Ensure that only Group1 (2) is visible on the screen. The rest of the groups should not be 
            // visble anymore
            verifyRowsWithTextAreDisplayed("Group1 (2)")
            verifyRowsWithTextDoesNotExist("Group2 (1)", "Group3 (2)")
        }
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_groups_of_colors() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select Colors
            clickRowWithText("Colors (4)")

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            // Enter "Dark Colors" in the search field
            inputTextWithTag("SearchTextField", "Dark Colors")

            // Ensure that no group is now visible on the screen. 
            verifyRowsWithTextDoesNotExist("Light Colors")
        }
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_groups_of_typography() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select Typography
            clickRowWithText("Typography (13)")

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            // Enter "Ho" in the search field
            inputTextWithTag("SearchTextField", "Ho")

            // Ensure that only "Holo" is visible on the screen. The rest of the groups should not be 
            // visble anymore
            verifyRowsWithTextAreDisplayed("Holo (3)")
            verifyRowsWithTextDoesNotExist("Light Colors")
        }
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_components() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select components
            clickRowWithText("Components (5)")

            // Select Group 3
            clickRowWithText("Group3 (2)")

            // Verify the right composables are visible on the screen
            verifyRowsWithTextAreDisplayed("Test Composable4", "Test Composable5")

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            // Enter "Composable4" in the search field
            inputTextWithTag("SearchTextField", "Composable4")

            // Ensure that only Composable4 is visible on the screen. The rest of the groups should not be 
            // visble anymore
            verifyRowsWithTextAreDisplayed("Test Composable4")
            verifyRowsWithTextDoesNotExist("Test Composable5")
        }
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_colors() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select "Colors"
            clickRowWithText("Colors (4)")

            // Click on "Light Colors"
            clickRowWithText("Light Colors (4)")

            // Verify the right colors are visible on the screen
            verifyColorsDetailScreen()

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            // Enter "Prim" in the search field
            inputTextWithTag("SearchTextField", "Prim")

            // Ensure that only "Primary" & "Primary Variant" is visible on the screen. The rest of the 
            // groups should not be visble anymore
            verifyRowsWithTextAreDisplayed("Primary Variant", "Primary")
            verifyRowsWithTextDoesNotExist("Secondary", "Secondary Variant")
        }
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_typography() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select "Typography"
            clickRowWithText("Typography (13)")

            // Select Material
            clickRowWithText("Material (10)")

            // Verify the right typography are visible on the screen
            verifyTypographyDetailScreen()

            // Swipe back to the top of the sreen
            onNodeWithTag("TypographyInAGroupList").performGesture {
                swipeDown()
            }

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            // Enter "Bod" in the search field
            inputTextWithTag("SearchTextField", "Bod")


            // Ensure that only "Body1" & "Body2" is visible on the screen. The rest of the groups should
            // not be visble anymore
            verifyRowsWithTextAreDisplayed("Body1", "Body2")
            verifyRowsWithTextDoesNotExist("H1", "H2", "H3", "H4", "H5", "H6", "Subtitle1",
                "Subtitle2")
        }
    }

    @Test
    fun navigating_to_component_leaf_screen_and_back_works_ok() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select components to go to the component groups screen
            clickRowWithText("Components (5)")

            // Click on "Group 1" to go to the components in a group screen
            clickRowWithText("Group1 (2)")
            
            // Click on "Test Composable1" to go to the component styles screen
            clickRowWithText("Test Composable1")

            waitForIdle()

            // Verify that the default style is visible on the screen
            verifyRowsWithTextAreDisplayed("Composable1 [Default Style]")

            // Click on "Test Composable1" to go to the component details screen
            clickRowWithText("Test Composable1")

            waitForIdle()

            // Go back to the component styles screen
            goBack()

            // Verify that the default style is visible on the screen
            verifyRowsWithTextAreDisplayed("Composable1 [Default Style]")

            // Go back to the components in a group screen
            goBack()

            // Confirm that we are in the right screen
            verifyRowsWithTextAreDisplayed("Test Composable1", "Test Composable2")

            // Go back to the component groups screen
            goBack()

            // Confirm that we are in the right screen
            verifyRowsWithTextAreDisplayed("Group1 (2)", "Group2 (1)", "Group3 (2)")

            // Go back to the landing screen
            goBack()

            // Confirm that we are in the right screen
            verifyLandingScreen()
        }
    }

    @Test
    fun navigating_to_color_leaf_screen_and_back_works_ok() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select "Colors" to go to the color groups screen
            clickRowWithText("Colors (4)")

            // Select "Light Colors" to go to the colors in a group screen
            clickRowWithText("Light Colors (4)")

            // Confirm that we are in the right screen
            verifyColorsDetailScreen()

            // Go back to the groups in color screen
            goBack()

            // Confirm that we are in the right screen
            verifyRowsWithTextAreDisplayed("Light Colors (4)")

            // Go back to the landing screen
            goBack()

            // Confirm that we are in the right screen
            verifyLandingScreen()
        }
    }

    @Test
    fun navigating_to_typography_leaf_screen_and_back_works_ok() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen()

            // Select "Typography" to go to the typography groups screen
            clickRowWithText("Typography (13)")

            // Select "Material" to go to the typography in a group screen
            clickRowWithText("Material (10)")

            // Confirm that we are in the right screen
            verifyTypographyDetailScreen()

            // Go back to the typography groups screen
            goBack()

            // Confirm that we are in the right screen
            verifyRowsWithTextAreDisplayed("Material (10)")

            // Go back to the landing screen
            goBack()

            // Confirm that we are in the right screen
            verifyLandingScreen()
        }
    }
}

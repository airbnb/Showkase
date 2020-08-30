package com.airbnb.android.showkase_processor_testing

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.ui.test.android.AndroidComposeTestRule
import androidx.ui.test.onNodeWithTag
import androidx.ui.test.performGesture
import androidx.ui.test.swipeDown
import androidx.ui.test.swipeUp
import androidx.ui.test.waitForIdle
import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
import com.vinaygaba.showcase_processor_testing.createShowkaseBrowserIntent
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
            ActivityScenarioRule<ShowkaseBrowserActivity>(
                createShowkaseBrowserIntent(
                    InstrumentationRegistry.getInstrumentation().targetContext
                )
            )
        )

    @Test
    fun activity_starts_and_all_the_showkase_ui_elements_are_visible_on_the_screen_and_clickable() {
        // Assert that all the groups are displayed on the screen and that they are clickable.
        verifyLandingScreen()
    }

    @Test
    fun clicking_components_takes_you_to_a_screen_with_groups_of_components() {
        // Click on one of the groups visible on the screen
        verifyLandingScreen()

        clickRowWithText("Components")

        verifyRowsWithTextAreDisplayed(listOf("Group1", "Group2", "Group3"))
    }

    @Test
    fun clicking_colors_takes_you_to_a_screen_with_groups_of_colors() {
        // Click on one of the groups visible on the screen
        verifyLandingScreen()

        clickRowWithText("Colors")

        verifyRowsWithTextAreDisplayed(listOf("Light Colors"))
    }

    @Test
    fun clicking_typography_takes_you_to_a_screen_with_groups_of_typography() {
        // Click on one of the groups visible on the screen
        verifyLandingScreen()

        clickRowWithText("Typography")

        verifyRowsWithTextAreDisplayed(listOf("Material", "Holo"))
    }

    @Test
    fun opening_component_detail_screen_has_5_permutations_displayed() {
        // Click on one of the groups visible on the screen
        verifyLandingScreen()

        clickRowWithText("Components")
        
        clickRowWithText("Group1")

        verifyRowsWithTextAreDisplayed(listOf("Test Composable1", "Test Composable2"))
        
        clickRowWithText("Test Composable1")

        verifyRowsWithTextAreDisplayed(
            listOf(
                "Composable1 [Basic Example]",
                "Composable1 [RTL]",
                "Composable1 [Font Scaled x 2]",
                "Composable1 [Display Scaled x 2]",
                "Composable1 [Dark Mode]"
            )
        )
        
        goBack(composeTestRule)

        clickRowWithText("Test Composable2")

        verifyRowsWithTextAreDisplayed(
            listOf(
                "Composable2 [Basic Example]",
                "Composable2 [RTL]",
                "Composable2 [Font Scaled x 2]",
                "Composable2 [Display Scaled x 2]",
                "Composable2 [Dark Mode]"
            )
        )
    }

    @Test
    fun selecting_color_group_has_colors_displayed() {
        // Click on one of the groups visible on the screen
        verifyLandingScreen()

        clickRowWithText("Colors")

        clickRowWithText("Light Colors")

        verifyColorsDetailScreen()
    }

    @Test
    fun selecting_typography_group_has_colors_displayed() {
        // Click on one of the groups visible on the screen
        verifyLandingScreen()

        clickRowWithText("Typography")

        clickRowWithText("Material")

        verifyTypographyDetailScreen()

        goBack(composeTestRule)

        clickRowWithText("Holo")

        verifyRowsWithTextAreDisplayed(listOf("Button", "Caption", "Overline"))
        
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_groups_of_components() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select Components
        clickRowWithText("Components")

        // Tap on the search icon
        clickRowWithTag("SearchIcon")

        // Enter "Group1" in the search field
        inputTextWithTag("SearchTextField", "group1")

        // Ensure that only Group1 is visible on the screen. The rest of the groups should not be 
        // visble anymore
        verifyRowsWithTextAreDisplayed(listOf("Group1"))
        verifyRowsWithTextDoesNotExist(listOf("Group3", "Group3"))
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_groups_of_colors() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select Colors
        clickRowWithText("Colors")

        // Tap on the search icon
        clickRowWithTag("SearchIcon")

        // Enter "Dark Colors" in the search field
        inputTextWithTag("SearchTextField", "Dark Colors")

        // Ensure that no group is visible on the screen. 
        verifyRowsWithTextDoesNotExist(listOf("Light Colors"))
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_groups_of_typography() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select Typography
        clickRowWithText("Typography")

        // Tap on the search icon
        clickRowWithTag("SearchIcon")

        // Enter "Group1" in the search field
        inputTextWithTag("SearchTextField", "Ho")

        // Ensure that only Group1 is visible on the screen. The rest of the groups should not be 
        // visble anymore
        verifyRowsWithTextAreDisplayed(listOf("Holo"))
        verifyRowsWithTextDoesNotExist(listOf("Light Colors"))
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_components() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select components
        clickRowWithText("Components")

        // Select Group 3
        clickRowWithText("Group3")

        // Verify the right composables are visible on the screen
        verifyRowsWithTextAreDisplayed(listOf("Test Composable4", "Test Composable5"))
        
        // Tap on the search icon
        clickRowWithTag("SearchIcon")

        // Enter "Composable4" in the search field
        inputTextWithTag("SearchTextField", "Composable4")

        // Ensure that only Composable4 is visible on the screen. The rest of the groups should not be 
        // visble anymore
        verifyRowsWithTextAreDisplayed(listOf("Test Composable4"))
        verifyRowsWithTextDoesNotExist(listOf("Test Composable5"))
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_colors() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select components
        clickRowWithText("Colors")

        // Select Light Colors
        clickRowWithText("Light Colors")

        // Verify the right colors are visible on the screen
        verifyColorsDetailScreen()

        // Tap on the search icon
        clickRowWithTag("SearchIcon")

        // Enter "Prim" in the search field
        inputTextWithTag("SearchTextField", "Prim")

        // Ensure that only Group1 is visible on the screen. The rest of the groups should not be 
        // visble anymore
        verifyRowsWithTextAreDisplayed(listOf("Primary Variant", "Primary"))
        verifyRowsWithTextDoesNotExist(listOf("Secondary", "Secondary Variant"))
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_typography() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select components
        clickRowWithText("Typography")

        // Select Light Colors
        clickRowWithText("Material")

        // Verify the right typography are visible on the screen
        verifyTypographyDetailScreen()

        onNodeWithTag("TypographyInAGroupList").performGesture {
            swipeDown()
        }
        waitForIdle()

        // Tap on the search icon
        clickRowWithTag("SearchIcon")

        // Enter "Subtitle" in the search field
        inputTextWithTag("SearchTextField", "Bod")
        
        waitForIdle()

        // Ensure that only Group1 is visible on the screen. The rest of the groups should not be 
        // visble anymore
        verifyRowsWithTextAreDisplayed(listOf("Body1", "Body2"))
        verifyRowsWithTextDoesNotExist(listOf("H1", "H2", "H3", "H4", "H5", "H6", "Subtitle1", 
            "Subtitle2"))
    }

    @Test
    fun navigating_to_component_leaf_screen_and_back_works_ok() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select components
        clickRowWithText("Components")

        // Select Group 3
        clickRowWithText("Group1")

        clickRowWithText("Test Composable1")
        
        goBack(composeTestRule)

        verifyRowsWithTextAreDisplayed(listOf("Test Composable1", "Test Composable2"))

        goBack(composeTestRule)

        verifyRowsWithTextAreDisplayed(listOf("Group1", "Group2", "Group3"))

        goBack(composeTestRule)
        
        verifyLandingScreen()
    }

    @Test
    fun navigating_to_color_leaf_screen_and_back_works_ok() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select components
        clickRowWithText("Colors")

        // Select Group 3
        clickRowWithText("Light Colors")

        verifyColorsDetailScreen()

        goBack(composeTestRule)
        
        verifyRowsWithTextAreDisplayed(listOf("Light Colors"))

        goBack(composeTestRule)

        verifyLandingScreen()
    }

    @Test
    fun navigating_to_typography_leaf_screen_and_back_works_ok() {
        // Ensure all the categories are visible
        verifyLandingScreen()

        // Select components
        clickRowWithText("Typography")

        // Select Group 3
        clickRowWithText("Material")

        verifyTypographyDetailScreen()

        goBack(composeTestRule)

        verifyRowsWithTextAreDisplayed(listOf("Material"))

        goBack(composeTestRule)

        verifyLandingScreen()
    }
}

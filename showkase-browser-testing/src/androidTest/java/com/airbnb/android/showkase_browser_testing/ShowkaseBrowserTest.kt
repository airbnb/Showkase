package com.airbnb.android.showkase_browser_testing

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.airbnb.android.showkase.models.Showkase
import com.airbnb.android.showkase.ui.ShowkaseBrowserActivity
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

    // This will alter now since KSP supports stacked preview annotations and KAPT does not.
    // It is not supported in KAPT because there is no support for repeatable annotations in KAPT
    // beyond the source retention KEEP, except for in the new IR backend which was introduced
    // in Kotlin 1.6. It will be available in the old backend in Kotlin version 1.7.20.
    // See https://youtrack.jetbrains.com/issue/KT-49682 for more information about this.
    private val componentSize = if (BuildConfig.IS_RUNNING_KSP) {
        20
    } else {
        10
    }

    @Test
    fun activity_starts_and_all_the_showkase_ui_elements_are_visible_on_the_screen_and_clickable() {
        // Assert that all the categories are displayed on the screen and that they are clickable.
        composeTestRule.verifyLandingScreen(
            components = componentSize,
            typography = 13,
            colors = 4,
        )
    }

    @Test
    fun clicking_components_takes_you_to_a_screen_with_groups_of_components() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            // Verify that all the groups are displayed on the screen
            verifyRowsWithTextAreDisplayed(
                "Group1 (2)",
                "Group2 (1)",
            )
        }
    }

    @Test
    fun clicking_colors_takes_you_to_a_screen_with_groups_of_colors() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Select Components
            clickRowWithText("Components ($componentSize)")

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Select components
            clickRowWithText("Components ($componentSize)")

            onRoot().performTouchInput {
                swipeUp()
            }

            waitForIdle()

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyRowsWithTextDoesNotExist(
                "H1", "H2", "H3", "H4", "H5", "H6", "Subtitle1",
                "Subtitle2"
            )
        }
    }

    @Test
    fun navigating_to_component_leaf_screen_and_back_works_ok() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Select components to go to the component groups screen
            clickRowWithText("Components ($componentSize)")

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
            verifyRowsWithTextAreDisplayed("Group1 (2)", "Group2 (1)")

            onRoot().performTouchInput {
                swipeUp()
            }

            waitForIdle()

            // Confirm that we are in the right screen
            verifyRowsWithTextAreDisplayed("Group3 (2)")


            // Go back to the landing screen
            goBack()

            // Confirm that we are in the right screen
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )
        }
    }

    @Test
    fun navigating_to_color_leaf_screen_and_back_works_ok() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )
        }
    }

    @Test
    fun navigating_to_typography_leaf_screen_and_back_works_ok() {
        composeTestRule.apply {
            // Ensure all the categories are visible
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

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
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )
        }
    }

    @Test
    fun components_with_long_names_have_a_correct_top_app_bar() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            onRoot().performTouchInput {
                swipeUp()
            }

            waitForIdle()

            // Select "Group4"
            clickRowWithText("Group4 (1)")

            // Select Component in question
            clickRowWithText("Test Composable6")

            waitForIdle()

            //Check that the top app bar wraps 3 lines
            verifyLineCountIsValue(3)
        }
    }

    @Test
    fun search_field_has_enabled_close_button() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            waitForIdle()

            // Check that the search close button is displayed and clickable
            verifyButtonWithTagIsDisplayedAndEnabled("close_search_bar_tag")

            // Click the close button
            clickRowWithTag("close_search_bar_tag")

            waitForIdle()

            // Check that the search icon is displayed again
            verifyButtonWithTagIsDisplayedAndEnabled("SearchIcon")
        }
    }

    @Test
    fun clear_search_field_clears_the_field() {
        composeTestRule.apply {
            // Assert that all the categories are displayed on the screen and that they are clickable.
            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )

            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            waitForIdle()

            // Tap on the search icon
            clickRowWithTag("SearchIcon")

            waitForIdle()

            // Check that the search close button is displayed and clickable
            verifyButtonWithTagIsDisplayedAndEnabled("close_search_bar_tag")

            // Enter "Bod" in the search field
            inputTextWithTag("SearchTextField", "Bod")

            // Check that the clear search field button is enabled when there is input text
            verifyButtonWithTagIsDisplayedAndEnabled("clear_search_field")

            // Click to clear the text
            clickRowWithTag("clear_search_field")

            // Check that only the label is displayed
            verifyRowsWithTextAreDisplayed("Search")

            waitForIdle()

            // Click the close button
            clickRowWithTag("close_search_bar_tag")

            // Check that the search icon is displayed again
            verifyButtonWithTagIsDisplayedAndEnabled("SearchIcon")
        }
    }

    @Test
    fun stacked_preview_show_up_in_browser() {
        // Stacked previews are only supported from ksp, so this is to bypass kapt on CI
        if (BuildConfig.IS_RUNNING_KSP) {
            composeTestRule.apply {

                verifyLandingScreen(
                    components = componentSize,
                    typography = 13,
                    colors = 4,
                )
                // Tap on the "Components" row
                clickRowWithText("Components ($componentSize)")

                waitForIdle()

                onRoot().performTouchInput {
                    swipeUp()
                }

                waitForIdle()

                clickRowWithText("Group7 (4)")

                waitForIdle()

                // Verify that they are all displayed and treated as different components
                onNodeWithText("Composable7").assertIsDisplayed()
                onNodeWithText("Composable8").assertIsDisplayed()
                onNodeWithText("Composable9").assertIsDisplayed()
                onNodeWithText("Composable10").assertIsDisplayed()

            }
        }
    }

    @Test
    fun customPreviewShowsUpInBrowser() {
        composeTestRule.apply {

            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )
            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            waitForIdle()

            clickRowWithText("Custom Text (1)")

            waitForIdle()

            // Verify that they are all displayed and treated as different components
            onNodeWithText("PreviewCustomTextLight - Custom Text Dark").assertIsDisplayed()

        }
    }

    @Test
    fun customSubmodulePreviewShowsUpInBrowser() {
        composeTestRule.apply {

            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )
            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            waitForIdle()

            onNodeWithText("Custom Size Submodule (1)").performScrollTo()

            waitForIdle()

            clickRowWithText("Custom Size Submodule (1)")

            waitForIdle()

            // Verify that they are all displayed and treated as different components
            onNodeWithText("CustomSubmoduleText - Custom Font Size 1.2f").assertIsDisplayed()

        }
    }

    @Test
    fun customStackedSubmodulePreviewShowsUpInBrowserForKsp() {
        if (BuildConfig.IS_RUNNING_KSP) {

            composeTestRule.apply {

                verifyLandingScreen(
                    components = componentSize,
                    typography = 13,
                    colors = 4,
                )
                // Tap on the "Components" row
                clickRowWithText("Components ($componentSize)")

                waitForIdle()

                clickRowWithText("CustomSubmodulePreview (2)")

                waitForIdle()

                // Verify that they are all displayed and treated as different components
                onNodeWithText("CustomShape - CustomSize 200 * 200").assertIsDisplayed()
                onNodeWithText("CustomShape - CustomSize 100 * 100").assertIsDisplayed()

            }
        }
    }

    @Test
    fun customStackedSubmoduleTwoPreviewShowsUpInBrowser() {

        composeTestRule.apply {

            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )
            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            waitForIdle()

            onRoot().performTouchInput {
                swipeUp()
            }

            waitForIdle()

            val composables = if (BuildConfig.IS_RUNNING_KSP) 3 else 1

            clickRowWithText("LocalePreview ($composables)")

            onNodeWithText("Some text In locale").assertIsDisplayed()

            waitForIdle()

        }
    }

    // We have enabled private preview compiler flag so this test
    // should test that we can have private methods annotated with custom annotations
    // and that they will not show in the application.
    @Test
    fun customAnnotatedPrivateComposablesShouldCompileButNotShow() {

        composeTestRule.apply {

            verifyLandingScreen(
                components = componentSize,
                typography = 13,
                colors = 4,
            )
            // Tap on the "Components" row
            clickRowWithText("Components ($componentSize)")

            waitForIdle()

            onRoot().performTouchInput {
                swipeUp()
            }

            waitForIdle()

            val composables = if (BuildConfig.IS_RUNNING_KSP) 3 else 1

            clickRowWithText("LocalePreview ($composables)")

            onNodeWithText("Private Text Composable").assertDoesNotExist()

            waitForIdle()

        }
    }
}

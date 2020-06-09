package com.vinaygaba.showcase_processor_testing

import androidx.ui.test.android.AndroidComposeTestRule
import androidx.ui.test.assertCountEquals
import androidx.ui.test.assertHasClickAction
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.children
import androidx.ui.test.doClick
import androidx.ui.test.doSendText
import androidx.ui.test.findBySubstring
import androidx.ui.test.findByTag
import androidx.ui.test.findByText
import androidx.ui.test.waitForIdle
import com.vinaygaba.showcase.ui.ShowcaseBrowserActivity
import org.junit.Before
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
        AndroidComposeTestRule<ShowcaseBrowserActivity>(disableTransitions = true)
    
    @Before
    fun setup() {
        restartActivity()
    }

    @Test
    fun activity_starts_and_all_the_groups_are_visible_on_the_screen_and_clickable() {
        // The showcase processor uses the composables defined in the TestComposables.kt file in the 
        // same module. The autogen class is then used by the ShowcaseBrowserActivity, which is what 
        // we are testing this these tests.
        
        // findByText is a helper method that looks for a composable component that contains the
        // text passed to it. It returns a SemanticsNodeInteraction, which allows us to do a
        // bunch of checks(isDisplayed, isToggelable, etc) and interactions(like click, scroll, etc)

        // Assert that all the groups are displayed on the screen and that they are clickable.
        findByText("Group1").assertIsDisplayed().assertHasClickAction()
        findByText("Group2").assertIsDisplayed().assertHasClickAction()
        findByText("Group3").assertIsDisplayed().assertHasClickAction()
    }

    @Test
    fun clicking_group_takes_you_to_a_screen_with_its_composables_and_they_are_displayed() {
        // The showcase processor uses the composables defined in the TestComposables.kt file in the 
        // same module. The autogen class is then used by the ShowcaseBrowserActivity, which is what 
        // we are testing this these tests.

        // findByText is a helper method that looks for a composable component that contains the
        // text passed to it. It returns a SemanticsNodeInteraction, which allows us to do a
        // bunch of checks(isDisplayed, isToggelable, etc) and interactions(like click, scroll, etc)

        // Click on one of the groups visible on the screen
        findByText("Group1").doClick()
        waitForIdle()

        findByText("Group1").assertIsDisplayed()
        findByTag("GroupComponentsList").children().assertCountEquals(4)
        
        findByText("Test Composable1").assertIsDisplayed()
        findByText("Test Composable2").assertIsDisplayed()
    }

    @Test
    fun opening_component_detail_screen_has_5_permutations_displayed() {
        // The showcase processor uses the composables defined in the TestComposables.kt file in the 
        // same module. The autogen class is then used by the ShowcaseBrowserActivity, which is what 
        // we are testing this these tests.

        // findByText is a helper method that looks for a composable component that contains the
        // text passed to it. It returns a SemanticsNodeInteraction, which allows us to do a
        // bunch of checks(isDisplayed, isToggelable, etc) and interactions(like click, scroll, etc)

        // Click on one of the groups visible on the screen
        findByText("Group1").doClick()
        waitForIdle()

        findByText("Test Composable1").doClick()
        waitForIdle()

        findBySubstring("[Basic Example]").assertIsDisplayed()
        findBySubstring("[Dark Mode]").assertIsDisplayed()
        findBySubstring("[RTL]").assertIsDisplayed()
        findBySubstring("[Font Scaled x 2]").assertIsDisplayed()
        findBySubstring("[Display Scaled x 2]").assertIsDisplayed()
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_groups() {
        // Ensure all the groups are visible
        findByText("Group1").assertIsDisplayed()
        findByText("Group2").assertIsDisplayed()
        findByText("Group3").assertIsDisplayed()
        
        // Tap on the search icon
        findByTag("SearchIcon").doClick()
        waitForIdle()
        
        // Enter "Group1" in the search field
        findByTag("SearchTextField").doSendText("Group1")
        waitForIdle()
        
        // Ensure that only Group1 is visible on the screen. The rest of the groups should not be 
        // visble anymore
        findByText("Group1").assertIsDisplayed()
        findByText("Group2").assertDoesNotExist()
        findByText("Group3").assertDoesNotExist()
    }

    @Test
    fun entering_text_in_search_bar_filters_the_visible_components() {
        // Click on one of the groups visible on the screen to go to the group components screen
        findByText("Group3").doClick()
        waitForIdle()
        
        // Ensure all the groups are visible
        findByText("Test Composable4").assertIsDisplayed()
        findByText("Test Composable5").assertIsDisplayed()

        // Tap on the search icon
        findByTag("SearchIcon").doClick()
        waitForIdle()

        // Enter "Group1" in the search field
        findByTag("SearchTextField").doSendText("Composable4")
        waitForIdle()

        // Ensure that only Group1 is visible on the screen. The rest of the groups should not be 
        // visble anymore
        findByText("Test Composable4").assertIsDisplayed()
        findByText("Test Composable5").assertDoesNotExist()
    }

    @Test
    fun navigating_to_leaf_screen_and_back_works_ok() {
        // Click on one of the groups visible on the screen to go to the group components screen
        findByText("Group1").doClick()
        waitForIdle()

        findByText("Test Composable1").doClick()
        waitForIdle()
        
        goBack()
        findByText("Test Composable1").assertIsDisplayed()
        findByText("Test Composable2").assertIsDisplayed()
        
        goBack()
        findByText("Group1").assertIsDisplayed()
        findByText("Group2").assertIsDisplayed()
        findByText("Group3").assertIsDisplayed()
    }

    private fun restartActivity() {
//        composeTestRule.activityTestRule.activity.finish()
//        composeTestRule.activityTestRule.activity.runOnUiThread {
//            composeTestRule.activityTestRule.activity.recreate()
//        }
//        composeTestRule.activityTestRule.activity.startActivity(
//            Intent(composeTestRule.activityTestRule.activity.applicationContext,
//                ShowcaseBrowserActivity::class.java))
    }
    
    private fun goBack() {
        composeTestRule.activityTestRule.activity.onBackPressed()
    }
}

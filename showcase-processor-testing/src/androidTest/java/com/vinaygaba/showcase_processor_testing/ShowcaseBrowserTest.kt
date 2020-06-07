package com.vinaygaba.showcase_processor_testing

import androidx.ui.test.android.AndroidComposeTestRule
import androidx.ui.test.assertAll
import androidx.ui.test.assertCountEquals
import androidx.ui.test.assertHasClickAction
import androidx.ui.test.assertIsDisplayed
import androidx.ui.test.doClick
import androidx.ui.test.findAll
import androidx.ui.test.findAllByText
import androidx.ui.test.findBySubstring
import androidx.ui.test.findByText
import androidx.ui.test.hasSubstring
import androidx.ui.test.hasText
import com.vinaygaba.showcase.ui.ShowcaseBrowserActivity
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

        findByText("Group1").assertIsDisplayed()
        findByText("Test Composable1").assertIsDisplayed()
        findByText("Test Composable2").assertIsDisplayed()
    }

    @Test
    fun clicking_groups_takes_you_to_a_screen_with_its_composables_and_they_are_displayed() {
        // The showcase processor uses the composables defined in the TestComposables.kt file in the 
        // same module. The autogen class is then used by the ShowcaseBrowserActivity, which is what 
        // we are testing this these tests.

        // findByText is a helper method that looks for a composable component that contains the
        // text passed to it. It returns a SemanticsNodeInteraction, which allows us to do a
        // bunch of checks(isDisplayed, isToggelable, etc) and interactions(like click, scroll, etc)

        // Click on one of the groups visible on the screen
        findByText("Group1").doClick()

        findByText("Test Composable1").doClick()
        
        findAllByText("Test Composable1").assertAll(hasSubstring("Test Composable1"))
        
        findAll(hasSubstring("Composable1")).assertCountEquals(5)
//        findAllByText("Test Composable1").assertCountEquals(5)
    }
}

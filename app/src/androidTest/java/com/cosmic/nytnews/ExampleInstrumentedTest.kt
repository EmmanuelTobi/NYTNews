package com.cosmic.nytnews

import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsMatcher.Companion.keyIsDefined
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.cosmic.nytnews", appContext.packageName)
    }

    @Test
    fun checkNewsTitleHeader() {
        composeTestRule.onNodeWithText("NYTimes News").assertIsDisplayed()
    }

    @Test
    fun testDropdownMenu() {

        composeTestRule.onNodeWithText("Yesterday").assertIsDisplayed()

        composeTestRule.onNode(isIconButton()).performClick()

        composeTestRule.onNodeWithText("Last 7 days").performClick()
        composeTestRule.onNodeWithText("Last 7 days").assertIsDisplayed()

        composeTestRule.onNode(isIconButton()).performClick()

        composeTestRule.onNodeWithText("Last 30 days").performClick()
        showLoadingIndicator(composeTestRule)
        composeTestRule.onNodeWithText("Last 30 days").assertIsDisplayed()

        waitForNewsToAppear(composeTestRule)

    }

    private fun isIconButton(): SemanticsMatcher {
        return keyIsDefined(SemanticsActions.OnClick)
    }

    private fun waitForNewsToAppear(composeTestRule: ComposeTestRule) {
        composeTestRule
            .onAllNodes(hasTestTag("NewsItem"))
            .fetchSemanticsNodes().isNotEmpty()
    }

    private fun showLoadingIndicator(composeTestRule: ComposeTestRule){
        composeTestRule.onNode(hasTestTag("LoadingIndicator")).assertIsDisplayed()
    }

}
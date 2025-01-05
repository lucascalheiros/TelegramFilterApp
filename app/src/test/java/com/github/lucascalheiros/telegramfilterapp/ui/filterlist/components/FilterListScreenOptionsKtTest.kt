package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FilterListScreenOptionsKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `on click show drop down`() {
        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterListScreenOptions(
                    {},
                    {}
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_MORE_TAG)
            .performClick()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_MORE_DROPDOWN_TAG)
            .assertIsDisplayed()
    }

    @Test
    fun `on logout callback should be called`() {
        var logoutConfirmed = false

        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterListScreenOptions(
                    {},
                    { logoutConfirmed = true }
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_MORE_TAG)
            .performClick()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_LOGOUT_TAG)
            .performClick()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_LOGOUT_DIALOG_TAG)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_LOGOUT_DIALOG_CONFIRM_TAG)
            .performClick()

        assert(logoutConfirmed)
    }

    @Test
    fun `on logout cancel dialog dismiss`() {
        var logoutConfirmed = false

        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterListScreenOptions(
                    {},
                    { logoutConfirmed = true }
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_MORE_TAG)
            .performClick()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_LOGOUT_TAG)
            .performClick()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_LOGOUT_DIALOG_TAG)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_LOGOUT_DIALOG_CANCEL_TAG)
            .performClick()

        assertFalse(logoutConfirmed)

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_LOGOUT_DIALOG_TAG)
            .isNotDisplayed()
    }

    @Test
    fun `on help callback should be called`() {
        var helpClicked = false

        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterListScreenOptions(
                    { helpClicked = true },
                    {}
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_MORE_TAG)
            .performClick()

        composeTestRule.onNodeWithTag(FilterListScreenOptionsTestFlags.FILTER_LIST_SCREEN_HELP_TAG)
            .performClick()

        assert(helpClicked)
    }


}
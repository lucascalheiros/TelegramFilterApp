package com.github.lucascalheiros.telegramfilterapp.ui.components

import android.provider.Settings
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class FilterMoreOptionsDropdownMenuKtTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testFilterOptionsDeleteConfirm() {
        var deleteConfirmed = false

        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterMoreOptionsDropdownMenu(
                    filterChannelType = mockk<ChannelType.FilteredMessage>(),
                    onFilterSettingsClick = {},
                    onDeleteFilterClick = { deleteConfirmed = true }
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.FILTER_MORE_BUTTON)
            .performClick()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.MORE_DROPDOWN_MENU)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.DELETE_MENU_OPTION)
            .performClick()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.DELETE_CONFIRMATION_ALERT)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.CONFIRM_DELETE)
            .performClick()

        assert(deleteConfirmed)

    }

    @Test
    fun testFilterOptionsDeleteCancel() {
        var deleteConfirmed = false

        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterMoreOptionsDropdownMenu(
                    filterChannelType = mockk<ChannelType.FilteredMessage>(),
                    onFilterSettingsClick = {},
                    onDeleteFilterClick = { deleteConfirmed = true }
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.FILTER_MORE_BUTTON)
            .performClick()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.MORE_DROPDOWN_MENU)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.DELETE_MENU_OPTION)
            .performClick()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.DELETE_CONFIRMATION_ALERT)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.CANCEL_DELETE)
            .performClick()

        TestCase.assertFalse(deleteConfirmed)

    }

    @Test
    fun testFilterOptionsEditAction() {
        var editPressed = false

        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterMoreOptionsDropdownMenu(
                    filterChannelType = mockk<ChannelType.FilteredMessage>(),
                    onFilterSettingsClick = { editPressed = true },
                    onDeleteFilterClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.FILTER_MORE_BUTTON)
            .performClick()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.MORE_DROPDOWN_MENU)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.EDIT_MENU_OPTION)
            .performClick()

        assert(editPressed)
    }

    @Test
    fun testFilterOptionsNotificationsAction() {
        val channelType = mockk<ChannelType.FilteredMessage>()

        every { channelType.channelId } returns "test"

        composeTestRule.setContent {
            TelegramFilterAppTheme {
                FilterMoreOptionsDropdownMenu(
                    filterChannelType = channelType,
                    onFilterSettingsClick = {},
                    onDeleteFilterClick = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.FILTER_MORE_BUTTON)
            .performClick()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.MORE_DROPDOWN_MENU)
            .assertIsDisplayed()
        Intents.init()

        composeTestRule.onNodeWithTag(FilterMoreOptionsDropdownMenuTestTags.NOTIFICATIONS_MENU_OPTION)
            .performClick()

        Intents.intended(IntentMatchers.hasAction(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS))

        Intents.release()
    }
}
package com.github.lucascalheiros.telegramfilterapp.ui.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenuTestTags.CANCEL_DELETE
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenuTestTags.CONFIRM_DELETE
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenuTestTags.DELETE_MENU_OPTION
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenuTestTags.EDIT_MENU_OPTION
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenuTestTags.FILTER_MORE_BUTTON
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenuTestTags.MORE_DROPDOWN_MENU
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenuTestTags.NOTIFICATIONS_MENU_OPTION
import com.github.lucascalheiros.telegramfilterapp.ui.util.openNotificationChannelSetting

@Composable
fun FilterMoreOptionsDropdownMenu(
    moreIcon: @Composable () -> Unit = {
        Icon(
            painterResource(R.drawable.ic_more_horiz),
            stringResource(R.string.more)
        )
    },
    filterChannelType: ChannelType?,
    onFilterSettingsClick: () -> Unit,
    onDeleteFilterClick: () -> Unit,
    isMoreOptionsExpanded: MutableState<Boolean> = remember { mutableStateOf(false) },
    showDeleteAlert: MutableState<Boolean> = remember { mutableStateOf(false) },
) {
    IconButton(
        { isMoreOptionsExpanded.value = true },
        modifier = Modifier.testTag(FILTER_MORE_BUTTON)
    ) {
        moreIcon()
    }
    if (showDeleteAlert.value) {
        DeleteConfirmationAlert(
            {
                showDeleteAlert.value = false
            },
            {
                showDeleteAlert.value = false
                onDeleteFilterClick()
            }
        )
    }
    DropdownMenu(
        expanded = isMoreOptionsExpanded.value,
        onDismissRequest = { isMoreOptionsExpanded.value = false },
        modifier = Modifier.testTag(MORE_DROPDOWN_MENU)
    ) {
        if (filterChannelType != null) {
            NotificationChannelSettingsOption(filterChannelType)
            HorizontalDivider()
        }
        FilterSettingsOption(onFilterSettingsClick)
        HorizontalDivider()
        DeleteFilterOption {
            showDeleteAlert.value = true
        }
    }
}

@Composable
private fun NotificationChannelSettingsOption(channelType: ChannelType) {
    DropdownMenuItem(
        text = {
            Text(stringResource(R.string.notification_settings))
        },
        leadingIcon = {
            Icon(painterResource(R.drawable.ic_notifications), null)
        },
        onClick = openNotificationChannelSetting(channelType),
        modifier = Modifier.testTag(NOTIFICATIONS_MENU_OPTION)
    )
}

@Composable
private fun FilterSettingsOption(onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(stringResource(R.string.edit))
        },
        leadingIcon = {
            Icon(painterResource(R.drawable.ic_edit), null)
        },
        onClick = onClick,
        modifier = Modifier.testTag(EDIT_MENU_OPTION)
    )
}

@Composable
private fun DeleteFilterOption(onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(stringResource(R.string.delete))
        },
        leadingIcon = {
            Icon(painterResource(R.drawable.ic_delete), null)
        },
        onClick = onClick,
        modifier = Modifier.testTag(DELETE_MENU_OPTION)
    )
}

@Composable
private fun DeleteConfirmationAlert(onCancel: () -> Unit, onConfirm: () -> Unit) {
    BaseDialog(
        title = { Text(stringResource(R.string.delete)) },
        content = { Text(stringResource(R.string.delete_filter_confirmation)) },
        actions = {
            TextButton(
                onCancel,
                Modifier.testTag(CANCEL_DELETE)
            ) { Text(stringResource(R.string.cancel)) }
            TextButton(
                onConfirm,
                Modifier.testTag(CONFIRM_DELETE)
            ) { Text(stringResource(R.string.delete)) }
        },
        onDismissRequest = onCancel,
        modifier = Modifier.testTag(FilterMoreOptionsDropdownMenuTestTags.DELETE_CONFIRMATION_ALERT)
    )
}

@Preview
@Composable
private fun FilterMoreOptionsPreview() {
    FilterMoreOptionsDropdownMenu(
        filterChannelType = ChannelType.FilteredMessage(
            Filter(
                0,
                "",
                listOf(),
                "",
                listOf(),
                0,
                FilterStrategy.TelegramQuerySearch
            )
        ),
        onFilterSettingsClick = {},
        onDeleteFilterClick = {},
        isMoreOptionsExpanded = remember { mutableStateOf(true) },
    )
}

object FilterMoreOptionsDropdownMenuTestTags {
    const val FILTER_MORE_BUTTON = "FILTER_MORE_BUTTON"
    const val MORE_DROPDOWN_MENU = "MORE_DROPDOWN_MENU"
    const val DELETE_MENU_OPTION = "DELETE_MENU_OPTION"
    const val NOTIFICATIONS_MENU_OPTION = "NOTIFICATIONS_MENU_OPTION"
    const val EDIT_MENU_OPTION = "EDIT_MENU_OPTION"
    const val DELETE_CONFIRMATION_ALERT = "DELETE_CONFIRMATION_ALERT"
    const val CANCEL_DELETE = "CANCEL_DELETE"
    const val CONFIRM_DELETE = "CONFIRM_DELETE"
}

package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.BaseDialog
import com.github.lucascalheiros.telegramfilterapp.ui.util.openNotificationSettings

@Composable
fun FilterListScreenOptions(onHelp: () -> Unit, onLogout: () -> Unit) {
    var isMoreOptionsExpanded by remember { mutableStateOf(false) }

    IconButton({ isMoreOptionsExpanded = true }) {
        Icon(painterResource(R.drawable.ic_more_vert), stringResource(R.string.more))
    }

    var showLogoutConfirmation by remember { mutableStateOf(false) }
    if (showLogoutConfirmation) {
        LogoutConfirmationAlert(
            onCancel = { showLogoutConfirmation = false },
            onConfirm = {
                showLogoutConfirmation = false
                onLogout()
            }
        )
    }
    DropdownMenu(
        expanded = isMoreOptionsExpanded,
        onDismissRequest = { isMoreOptionsExpanded = false }
    ) {
        HelpMenuItem(onHelp)
        HorizontalDivider()
        NotificationSettingsMenuItem(openNotificationSettings())
        HorizontalDivider()
        LogoutMenuItem {
            isMoreOptionsExpanded = false
            showLogoutConfirmation = true
        }
    }
}

@Composable
private fun HelpMenuItem(onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.help)) },
        onClick = onClick,
        leadingIcon = { Icon(painterResource(R.drawable.ic_help), null) }
    )
}

@Composable
private fun NotificationSettingsMenuItem(onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.notifications)) },
        onClick = onClick,
        leadingIcon = { Icon(painterResource(R.drawable.ic_notifications), null) }
    )
}

@Composable
private fun LogoutMenuItem(onClick: () -> Unit) {
    DropdownMenuItem(
        text = { Text(stringResource(R.string.logout)) },
        onClick = onClick,
        leadingIcon = { Icon(painterResource(R.drawable.ic_logout), null) }
    )
}

@Composable
private fun LogoutConfirmationAlert(onCancel: () -> Unit, onConfirm: () -> Unit) {
    BaseDialog(
        title = { Text(stringResource(R.string.logout)) },
        content = { Text(stringResource(R.string.the_current_filters_will_be_deleted_on_logout_to_proceed_press_yes)) },
        actions = {
            TextButton(onCancel) { Text(stringResource(R.string.cancel)) }
            TextButton(onConfirm) { Text(stringResource(R.string.logout)) }
        },
        onDismissRequest = onCancel
    )
}

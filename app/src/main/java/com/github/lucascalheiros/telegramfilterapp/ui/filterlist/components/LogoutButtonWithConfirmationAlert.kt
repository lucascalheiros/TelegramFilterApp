package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components

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

@Composable
fun LogoutButtonWithConfirmationAlert(onLogout: () -> Unit) {
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
    IconButton({ showLogoutConfirmation = true }) {
        Icon(
            painterResource(R.drawable.ic_logout),
            stringResource(R.string.logout)
        )
    }
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


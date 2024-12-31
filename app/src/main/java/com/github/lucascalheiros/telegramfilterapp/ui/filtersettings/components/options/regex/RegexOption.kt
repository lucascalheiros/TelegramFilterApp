package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.regex

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.SettingItem

@Composable
fun RegexOption(regex: String, onUpdateRegex: (String) -> Unit) {
    var showInputDialog by remember { mutableStateOf(false) }
    SettingItem(stringResource(R.string.regex), { showInputDialog = true }) {
        Text(regex, color = MaterialTheme.colorScheme.primary)
    }
    if (showInputDialog) {
        RegexInputDialog(
            { showInputDialog = false },
            {
                showInputDialog = false
                onUpdateRegex(it)
            }
        )
    }
}
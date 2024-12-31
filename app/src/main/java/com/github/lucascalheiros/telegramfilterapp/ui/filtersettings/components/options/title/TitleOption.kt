package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.title

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
fun TitleOption(title: String, onUpdateTitle: (String) -> Unit) {
    var showTitleInputDialog by remember { mutableStateOf(false) }
    SettingItem(stringResource(R.string.title), { showTitleInputDialog = true }) {
        Text(title, color = MaterialTheme.colorScheme.primary)
    }
    if (showTitleInputDialog) {
        TitleInputDialog(
            { showTitleInputDialog = false },
            {
                showTitleInputDialog = false
                onUpdateTitle(it)
            }
        )
    }
}
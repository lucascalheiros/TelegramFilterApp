package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingItem(title: String, onClick: (() -> Unit)? = null, value: @Composable () -> Unit) {
    Row(Modifier
        .run {
            onClick?.let {
                clickable { it() }
            } ?: this
        }
        .padding(vertical = 16.dp, horizontal = 12.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.weight(1f))
        value()
    }
}
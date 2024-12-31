package com.github.lucascalheiros.telegramfilterapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
        .padding(horizontal = 12.dp)
        .heightIn(48.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.weight(1f))
        value()
    }
}
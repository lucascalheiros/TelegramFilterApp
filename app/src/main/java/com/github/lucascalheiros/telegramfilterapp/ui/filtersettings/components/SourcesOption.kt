package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsIntent

@Composable
fun SourcesOption(onlyChannels: Boolean, dispatch: (FilterSettingsIntent) -> Unit) {
    var sourceMenuExpanded by remember { mutableStateOf(false) }
    SettingItem(stringResource(R.string.filter_message_source), { sourceMenuExpanded = true }) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
        ) {
            val res = if (onlyChannels) R.string.all_channels else R.string.selection
            Text(stringResource(res), color = MaterialTheme.colorScheme.primary)
            DropdownMenu(
                expanded = sourceMenuExpanded,
                onDismissRequest = { sourceMenuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.all_channels)) },
                    onClick = {
                        dispatch(FilterSettingsIntent.SetAllChannelsState(true))
                        sourceMenuExpanded = false
                    },
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.selection)) },
                    onClick = {
                        dispatch(FilterSettingsIntent.SetAllChannelsState(false))
                        sourceMenuExpanded = false
                    },
                )
            }
        }
    }
}
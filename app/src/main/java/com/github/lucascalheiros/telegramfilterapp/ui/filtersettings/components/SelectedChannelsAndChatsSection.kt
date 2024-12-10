package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsIntent
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState

@Composable
fun SelectedChannelsAndChatsSection(
    state: FilterSettingsUiState,
    onRemoveChat: (Long) -> Unit,
    onShowChatSelector: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.selected_channels_chats), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(16.dp))
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(state.selectedChats, { it.id }) {
                ListItem(
                    headlineContent = {
                        Text(it.title)
                    },
                    trailingContent = {
                        IconButton(
                            { onRemoveChat(it.id) }
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_close),
                                stringResource(R.string.remove)
                            )
                        }
                    }
                )
                HorizontalDivider()
            }
            item {
                TextButton(
                    onClick = onShowChatSelector,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painterResource(R.drawable.ic_add),
                        null
                    )
                    Text(stringResource(R.string.add_message_source))
                }
            }
        }
    }
}
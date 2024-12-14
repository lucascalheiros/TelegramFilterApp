package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState

@Composable
fun SelectedChannelsAndChatsSection(
    state: FilterSettingsUiState,
    onRemoveChat: (Long) -> Unit,
    onSetSelectedChats: (List<Long>) -> Unit
) {
    var showSelectChatsDialog by remember { mutableStateOf(false) }
    if (showSelectChatsDialog) {
        SelectChatBottomSheet(
            availableChats = state.availableChats,
            selectedChats = state.selectedChats,
            onCancel = {
                showSelectChatsDialog = false
            },
            onConfirm = {
                showSelectChatsDialog = false
                onSetSelectedChats(it)
            }
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalDivider()
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            items(state.selectedChats, { it.id }) {
                Row(
                    Modifier.padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(it.title, Modifier.fillMaxWidth(0.7f), overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        { onRemoveChat(it.id) },
                        Modifier.defaultMinSize(44.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_close),
                            stringResource(R.string.remove)
                        )
                    }
                }
                HorizontalDivider()
            }
            item {
                TextButton(
                    onClick = {
                        showSelectChatsDialog = true
                    },
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
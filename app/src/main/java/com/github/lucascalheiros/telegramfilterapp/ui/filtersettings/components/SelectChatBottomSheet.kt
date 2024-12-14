package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.telegramfilterapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectChatBottomSheet(
    availableChats: List<ChatInfo>,
    selectedChats: List<ChatInfo>,
    onCancel: () -> Unit,
    onConfirm: (List<Long>) -> Unit
) {
    var selectedChatIds by remember { mutableStateOf(selectedChats.map { it.id }) }
    ModalBottomSheet(
        onDismissRequest = onCancel
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TextButton(
                    onClick = { onCancel() },
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Text(
                    stringResource(R.string.select_chats),
                    style = MaterialTheme.typography.titleLarge
                )
                TextButton(
                    onClick = {
                        onConfirm(availableChats.mapNotNull { chatInfo ->
                            chatInfo.id.takeIf { it in selectedChatIds }
                        })
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            LazyColumn {
                items(availableChats, { it.id }) { chatInfo ->
                    var isChecked by remember { mutableStateOf(chatInfo.id in selectedChatIds) }
                    ListItem(
                        headlineContent = {
                            Text(chatInfo.title)
                        }, leadingContent = {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = {
                                    isChecked = it
                                    selectedChatIds = if (it) {
                                        selectedChatIds.toMutableList().apply {
                                            add(chatInfo.id)
                                        }
                                    } else {
                                        selectedChatIds.toMutableList().apply {
                                            remove(chatInfo.id)
                                        }
                                    }
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}
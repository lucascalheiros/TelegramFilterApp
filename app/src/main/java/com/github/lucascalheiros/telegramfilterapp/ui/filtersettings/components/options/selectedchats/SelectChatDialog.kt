package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.selectedchats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.ChatType
import com.github.lucascalheiros.telegramfilterapp.R

@Composable
fun SelectChatDialog(
    availableChats: List<ChatInfo>,
    onCancel: () -> Unit,
    onConfirm: (chatIdsToAdd: List<Long>) -> Unit
) {
    var selectedChatIds by remember { mutableStateOf(listOf<Long>()) }
    var searchChatText by remember { mutableStateOf("") }
    val filteredChats =
        availableChats.filter { it.title.contains(searchChatText, ignoreCase = true) }
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onCancel,
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    TextButton(
                        onClick = { onCancel() },
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Text(
                        stringResource(R.string.add_chats),
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
                OutlinedTextField(
                    value = searchChatText,
                    onValueChange = { searchChatText = it },
                    leadingIcon = { Icon(painterResource(R.drawable.ic_search), null) },
                    label = { Text(stringResource(R.string.search)) }
                )
                Spacer(Modifier.height(8.dp))
                HorizontalDivider()
                LazyColumn {
                    item {
                        ListItem(
                            headlineContent = {
                                Text(stringResource(R.string.channels))
                            }
                        )
                    }
                    items(
                        filteredChats.filter { it.chatType == ChatType.Channel },
                        { it.id }) { chatInfo ->
                        ChatItem(chatInfo, selectedChatIds) {
                            selectedChatIds = it
                        }
                    }
                    item {
                        ListItem(
                            headlineContent = {
                                Text(stringResource(R.string.groups))
                            }
                        )
                    }
                    items(
                        filteredChats.filter { it.chatType == ChatType.Group },
                        { it.id }) { chatInfo ->
                        ChatItem(chatInfo, selectedChatIds) {
                            selectedChatIds = it
                        }
                    }
                    item {
                        ListItem(
                            headlineContent = {
                                Text(stringResource(R.string.chats))
                            }
                        )
                    }
                    items(
                        filteredChats.filter { it.chatType == ChatType.Chat },
                        { it.id }) { chatInfo ->
                        ChatItem(chatInfo, selectedChatIds) {
                            selectedChatIds = it
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chatInfo: ChatInfo,
    selectedChatIds: List<Long>,
    onUpdateSelection: (List<Long>) -> Unit
) {
    var isChecked by remember { mutableStateOf(chatInfo.id in selectedChatIds) }

    val onSelect = {
        isChecked = !isChecked
        selectedChatIds.toMutableList().apply {
            if (isChecked) {
                add(chatInfo.id)
            } else {
                remove(chatInfo.id)
            }
        }.run(onUpdateSelection)
    }
    ListItem(
        modifier = Modifier.selectable(isChecked, true, Role.Checkbox, onSelect),
        headlineContent = {
            Text(chatInfo.title)
        }, leadingContent = {
            Checkbox(
                checked = isChecked,
                onCheckedChange = null
            )
        }
    )
}
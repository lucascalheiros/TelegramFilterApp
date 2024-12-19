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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.ChatType
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
    var searchChatText by remember { mutableStateOf("") }
    val filteredChats =
        availableChats.filter { it.title.contains(searchChatText, ignoreCase = true) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    ) {
        it != SheetValue.Hidden
    }

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        onConfirm(filteredChats.mapNotNull { chatInfo ->
                            chatInfo.id.takeIf { it in selectedChatIds }
                        })
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = searchChatText,
                onValueChange = { searchChatText = it },
                leadingIcon = { Icon(painterResource(R.drawable.ic_search), null) },
                label = { Text(stringResource(R.string.search)) }
            )
            HorizontalDivider()
            LazyColumn {
                item {
                    ListItem(
                        headlineContent = {
                            Text(stringResource(R.string.channels))
                        }
                    )
                }
                items(filteredChats.filter { it.chatType == ChatType.Channel }, { it.id }) { chatInfo ->
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
                items(filteredChats.filter { it.chatType == ChatType.Group }, { it.id }) { chatInfo ->
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
                items(filteredChats.filter { it.chatType == ChatType.Chat }, { it.id }) { chatInfo ->
                    ChatItem(chatInfo, selectedChatIds) {
                        selectedChatIds = it
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
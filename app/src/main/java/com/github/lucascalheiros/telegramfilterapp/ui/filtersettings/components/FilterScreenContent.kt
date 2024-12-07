package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsIntent
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterScreenContent(
    state: FilterSettingsUiState,
    send: (FilterSettingsIntent) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    if (state.showAddQueryDialog) {
        AddQueryDialog(
            onCancel = {
                send(FilterSettingsIntent.DismissAddQuery)
            },
            onConfirm = {
                send(FilterSettingsIntent.AddQuery(it))
            }
        )
    }

    if (state.showSelectChatDialog) {
        SelectChatDialog(
            availableChats = state.availableChats,
            selectedChats = state.selectedChats,
            onCancel = {
                send(FilterSettingsIntent.DismissSelectChat)
            },
            onConfirm = {
            }
        )
    }
    Scaffold(
        topBar = {
            FilterSettingsTopBar(onBackPressed)
        },
        floatingActionButton = {
            FloatingActionButton({
                send(FilterSettingsIntent.Save)
            }) {
                Icon(painterResource(R.drawable.ic_save), stringResource(R.string.save))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .width(280.dp),
            ) {
                TextField(
                    value = state.filterTitle,
                    onValueChange = { send(FilterSettingsIntent.UpdateTitle(it)) },
                    label = { Text(stringResource(R.string.title)) }
                )
                Spacer(Modifier.height(16.dp))
                FlowRow(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    state.queries.forEachIndexed { index, value ->
                        InputChip(
                            modifier = Modifier.padding(4.dp),
                            selected = true,
                            onClick = {
                                send(FilterSettingsIntent.RemoveQuery(index))
                            },
                            label = { Text(value) },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = stringResource(R.string.remove_query),
                                    Modifier.size(InputChipDefaults.AvatarSize)
                                )
                            }
                        )
                    }
                }
                TextButton(
                    { send(FilterSettingsIntent.ShowAddQuery) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painterResource(R.drawable.ic_add),
                        null
                    )
                    Text(stringResource(R.string.query))
                }
                Spacer(Modifier.height(16.dp))
                Text(stringResource(R.string.filter_message_source))
                CheckboxWithLabel(
                    state.allChannels,
                    { send(FilterSettingsIntent.ToggleAllChannels) },
                    stringResource(R.string.all_channels)
                )
                Text("Specified sources")
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                    items(state.selectedChats, { it.id }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(it.title)
                            Spacer(Modifier.weight(1f))
                            IconButton({}) {
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
                            { send(FilterSettingsIntent.ShowSelectChat) },
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
    }
}

@Preview
@Composable
private fun FilterScreenContentPreview() {
    TelegramFilterAppTheme {
        FilterScreenContent(
            FilterSettingsUiState(
                filterTitle = "Nintendo Sports",
                selectedChats = listOf(ChatInfo(0, "Test"))
            )
        ) {

        }
    }
}


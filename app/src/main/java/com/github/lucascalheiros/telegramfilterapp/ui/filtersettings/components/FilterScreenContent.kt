package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@Composable
fun FilterScreenContent(
    state: FilterSettingsUiState,
    dispatch: (FilterSettingsIntent) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    if (state.showAddQueryDialog) {
        AddQueryDialog(
            onCancel = {
                dispatch(FilterSettingsIntent.DismissAddQuery)
            },
            onConfirm = {
                dispatch(FilterSettingsIntent.AddQuery(it))
            }
        )
    }

    if (state.showSelectChatDialog) {
        SelectChatDialog(
            availableChats = state.availableChats,
            selectedChats = state.selectedChats,
            onCancel = {
                dispatch(FilterSettingsIntent.DismissSelectChat)
            },
            onConfirm = {
                dispatch(FilterSettingsIntent.SetSelectedChats(it))
            }
        )
    }
    Scaffold(
        topBar = {
            FilterSettingsTopBar(onBackPressed)
        },
        floatingActionButton = {
            FloatingActionButton({
                dispatch(FilterSettingsIntent.Save)
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
            TextField(
                value = state.filterTitle,
                onValueChange = { dispatch(FilterSettingsIntent.UpdateTitle(it)) },
                label = { Text(stringResource(R.string.title)) }
            )
            Spacer(Modifier.height(16.dp))
            QueriesList(
                queries = state.queries,
                onRemoveIndex = { dispatch(FilterSettingsIntent.RemoveQuery(it)) }
            )
            TextButton(
                { dispatch(FilterSettingsIntent.ShowAddQuery) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(painterResource(R.drawable.ic_add), null)
                Text(stringResource(R.string.query))
            }
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.filter_message_source),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(16.dp))
            Column(Modifier.selectableGroup()) {
                RadioWithText(
                    selected = state.onlyChannels,
                    onClick = { dispatch(FilterSettingsIntent.SetAllChannelsState(true)) },
                    text = stringResource(R.string.all_channels)
                )
                Spacer(Modifier.height(8.dp))
                RadioWithText(
                    selected = !state.onlyChannels,
                    onClick = { dispatch(FilterSettingsIntent.SetAllChannelsState(false)) },
                    text = stringResource(R.string.selected_chats)
                )
            }
            AnimatedVisibility(!state.onlyChannels) {
                SelectedChannelsAndChatsSection(
                    state,
                    { dispatch(FilterSettingsIntent.RemoveChat(it)) },
                    { dispatch(FilterSettingsIntent.ShowSelectChat) }
                )
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
                availableChats = listOf(ChatInfo(0, "Test")),
                selectedChatIds = listOf(0)
            )
        ) {

        }
    }
}


package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.ChatType
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
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                TitleOption(state.filterTitle, dispatch)
                HorizontalDivider()
                QueriesOption(state.queries, dispatch)
                HorizontalDivider()
                FilterDateTimeOption(state.filterDateTime, dispatch)
                HorizontalDivider()
                SourcesOption(state.onlyChannels, dispatch)
                AnimatedVisibility(!state.onlyChannels) {
                    Column {
                        HorizontalDivider()
                        SettingItem(stringResource(R.string.selected_sources)) {
                            Text(
                                state.selectedChatIds.size.toString(),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        SelectedChannelsAndChatsSection(
                            state,
                            { dispatch(FilterSettingsIntent.RemoveChat(it)) },
                            { dispatch(FilterSettingsIntent.SetSelectedChats(it)) }
                        )
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
                availableChats = listOf(ChatInfo(0, "Test", ChatType.Chat)),
                selectedChatIds = listOf(0)
            )
        ) {

        }
    }
}


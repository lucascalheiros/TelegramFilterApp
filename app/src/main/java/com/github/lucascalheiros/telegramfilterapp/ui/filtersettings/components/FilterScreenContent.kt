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
import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsIntent
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.filterdatetime.FilterDateTimeOption
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.filterstrategy.FilterStrategyOption
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.queries.QueriesOption
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.regex.RegexOption
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.selectedchats.SelectedChatsOption
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.title.TitleOption
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
                TitleOption(state.filterTitle) {
                    dispatch(FilterSettingsIntent.UpdateTitle(it))
                }
                HorizontalDivider()
                FilterStrategyOption(state.strategy) {
                    dispatch(FilterSettingsIntent.SetFilterStrategy(it))
                }
                HorizontalDivider()
                AnimatedVisibility(state.strategy == FilterStrategy.TelegramQuerySearch) {
                    QueriesOption(
                        state.queries,
                        { dispatch(FilterSettingsIntent.RemoveQuery(it)) },
                        { dispatch(FilterSettingsIntent.AddQuery(it)) }
                    )
                }
                AnimatedVisibility(state.strategy == FilterStrategy.LocalRegexSearch) {
                    RegexOption(state.regex) {
                        dispatch(FilterSettingsIntent.UpdateRegex(it))
                    }
                }
                HorizontalDivider()
                FilterDateTimeOption(state.filterDateTime) {
                    dispatch(FilterSettingsIntent.SetFilterDateTime(it))
                }
                HorizontalDivider()
                Column {
                    HorizontalDivider()
                    SettingItem(stringResource(R.string.selected_sources)) {
                        Text(
                            state.selectedChatIds.size.toString(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    SelectedChatsOption(
                        state,
                        { dispatch(FilterSettingsIntent.RemoveChat(it)) },
                        { dispatch(FilterSettingsIntent.AddSelectedChats(it)) }
                    )
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
            state = FilterSettingsUiState(
                filterTitle = stringResource(R.string.new_filter),
                allAvailableChats = listOf(ChatInfo(0, "Test", ChatType.Chat)),
                selectedChatIds = listOf(0)
            )
        ) {

        }
    }
}


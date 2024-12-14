package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer

import com.github.lucascalheiros.telegramfilterapp.ui.Reducer
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.AddQuery
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.Close
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.RemoveChat
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.RemoveQuery
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.SetAllChannelsState
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.SetFilter
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.SetFilterDateTime
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.SetSelectedChats
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.UpdateAvailableChats
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.UpdateTitle
import com.github.lucascalheiros.common.datetime.millisToLocalDateTime
import javax.inject.Inject


class FilterSettingsReducer @Inject constructor() :
    Reducer<FilterSettingsUiState, FilterSettingsAction> {

    override fun reduce(
        state: FilterSettingsUiState, action: FilterSettingsAction
    ): FilterSettingsUiState {
        return when (action) {
            is SetAllChannelsState -> state.copy(
                onlyChannels = action.state
            )

            is UpdateTitle -> state.copy(
                filterTitle = action.title
            )

            is SetFilter -> state.copy(
                filterTitle = action.filter.title,
                queries = action.filter.queries,
                onlyChannels = action.filter.onlyChannels,
                selectedChatIds = action.filter.chatIds,
                filterDateTime = action.filter.dateLimit.millisToLocalDateTime()
            )

            Close -> state.copy(close = true)

            is AddQuery -> state.copy(
                queries = state.queries.toMutableList().apply {
                    add(action.text)
                },
            )

            is RemoveQuery -> state.copy(
                queries = state.queries.toMutableList().apply {
                    removeAt(action.index)
                }
            )

            is UpdateAvailableChats -> state.copy(
                availableChats = action.chats
            )

            is SetSelectedChats -> state.copy(
                selectedChatIds = action.chatIds,
            )

            is RemoveChat -> state.copy(
                selectedChatIds = state.selectedChatIds.filter { it != action.chatId }
            )

            is SetFilterDateTime -> state.copy(
                filterDateTime = action.dateTime
            )
        }
    }

}


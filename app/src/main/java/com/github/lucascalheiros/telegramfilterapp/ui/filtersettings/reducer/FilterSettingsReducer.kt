package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer

import com.github.lucascalheiros.telegramfilterapp.ui.base.Reducer
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.AddQuery
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.Close
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.RemoveChat
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.RemoveQuery
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.SetFilter
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.SetFilterDateTime
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.AddSelectedChats
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
            is UpdateTitle -> state.copy(
                filterTitle = action.title
            )

            is SetFilter -> state.copy(
                filterTitle = action.filter.title,
                queries = action.filter.queries,
                regex = action.filter.regex,
                strategy = action.filter.strategy,
                selectedChatIds = action.filter.chatIds,
                filterDateTime = action.filter.limitDate.millisToLocalDateTime()
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
                allAvailableChats = action.chats
            )

            is AddSelectedChats -> state.copy(
                selectedChatIds = state.selectedChatIds + action.chatIds,
            )

            is RemoveChat -> state.copy(
                selectedChatIds = state.selectedChatIds.filter { it != action.chatId }
            )

            is SetFilterDateTime -> state.copy(
                filterDateTime = action.dateTime
            )

            is FilterSettingsAction.SetFilterStrategy -> state.copy(strategy = action.strategy)

            is FilterSettingsAction.UpdateRegex -> state.copy(regex = action.regex)
        }
    }

}


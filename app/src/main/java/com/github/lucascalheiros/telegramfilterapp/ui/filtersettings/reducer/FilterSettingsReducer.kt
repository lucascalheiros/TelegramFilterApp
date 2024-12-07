package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer

import com.github.lucascalheiros.telegramfilterapp.ui.Reducer
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction.*
import javax.inject.Inject


class FilterSettingsReducer @Inject constructor() :
    Reducer<FilterSettingsUiState, FilterSettingsAction> {

    override fun reduce(
        state: FilterSettingsUiState, action: FilterSettingsAction
    ): FilterSettingsUiState {
        return when (action) {
            ToggleAllChannels -> state.copy(allChannels = true)

            is UpdateTitle -> state.copy(filterTitle = action.title)
            is SetFilter -> state.copy(
                filterTitle = action.filter.title,
                queries = action.filter.queries,
            )

            Close -> state.copy(close = true)
            is AddQuery -> state.copy(queries = state.queries.toMutableList().apply {
                add(action.text)
            }, showAddQueryDialog = false)

            ShowAddQuery -> state.copy(showAddQueryDialog = true)
            DismissAddQuery -> state.copy(showAddQueryDialog = false)
            is RemoveQuery -> state.copy(queries = state.queries.toMutableList().apply {
                removeAt(action.index)
            })

            DismissSelectChat -> state.copy(showSelectChatDialog = false)
            ShowSelectChat -> state.copy(showSelectChatDialog = true)
            is UpdateAvailableChats -> state.copy(availableChats = action.chats)
        }
    }

}


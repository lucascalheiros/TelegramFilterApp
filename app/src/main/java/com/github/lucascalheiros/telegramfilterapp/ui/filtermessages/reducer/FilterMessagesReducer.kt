package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer

import com.github.lucascalheiros.telegramfilterapp.ui.base.Reducer
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesUiState
import javax.inject.Inject

class FilterMessagesReducer @Inject constructor() :
    Reducer<FilterMessagesUiState, FilterMessagesAction> {

    override fun reduce(
        state: FilterMessagesUiState,
        action: FilterMessagesAction
    ): FilterMessagesUiState {
        return when (action) {
            is FilterMessagesAction.SetMessages -> state.copy(
                messages = action.messages,
                isLoadingMessages = false
            )

            FilterMessagesAction.LoadingMessage -> state.copy(
                isLoadingMessages = true
            )

            FilterMessagesAction.Close -> state.copy(close = true)

            is FilterMessagesAction.SetFilter -> state.copy(filter = action.filter)
        }
    }

}
package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message

sealed interface FilterMessagesAction {
    data class SetMessages(val messages: List<Message>): FilterMessagesAction
    data class SetFilter(val filter: Filter): FilterMessagesAction
    data object LoadingMessage: FilterMessagesAction
    data object Close: FilterMessagesAction
}
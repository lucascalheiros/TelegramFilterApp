package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer

import com.github.lucascalheiros.domain.model.Message

sealed interface FilterMessagesAction {
    data class SetMessages(val messages: List<Message>): FilterMessagesAction
}
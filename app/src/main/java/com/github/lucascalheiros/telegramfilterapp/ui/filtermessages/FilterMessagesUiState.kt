package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages

import com.github.lucascalheiros.domain.model.Message

data class FilterMessagesUiState(
    val messages: List<Message> = listOf(),
    val isLoadingMessages: Boolean = false
) {
    val showNoMessagesInfo: Boolean by lazy {
        messages.isEmpty() && !isLoadingMessages
    }
}
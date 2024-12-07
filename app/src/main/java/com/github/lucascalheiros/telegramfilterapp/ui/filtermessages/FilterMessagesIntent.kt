package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages

sealed interface FilterMessagesIntent {
    data object LoadData: FilterMessagesIntent
}
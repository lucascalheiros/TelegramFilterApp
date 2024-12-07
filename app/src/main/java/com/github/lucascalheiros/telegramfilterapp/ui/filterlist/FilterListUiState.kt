package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.Filter

data class FilterListUiState(
    val chats: List<ChatInfo> = listOf(),
    val filters: List<Filter> = listOf()
)

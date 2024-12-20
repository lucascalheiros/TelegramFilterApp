package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.FilterStrategy
import java.time.LocalDateTime

data class FilterSettingsUiState(
    val filterTitle: String = "",
    val filterDateTime: LocalDateTime = LocalDateTime.now(),
    val strategy: FilterStrategy = FilterStrategy.TelegramQuerySearch,
    val queries: List<String> = listOf(),
    val regex: String = "",
    val availableChats: List<ChatInfo> = listOf(),
    val selectedChatIds: List<Long> = listOf(),
    val close: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() {
            return queries.isNotEmpty() && selectedChats.isNotEmpty()
        }

    val selectedChats: List<ChatInfo> by lazy {
        availableChats.filter { it.id in selectedChatIds }
    }

}
package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import com.github.lucascalheiros.domain.model.ChatInfo
import java.time.LocalDateTime

data class FilterSettingsUiState(
    val filterTitle: String = "",
    val filterDateTime: LocalDateTime = LocalDateTime.now(),
    val queries: List<String> = listOf(),
    val onlyChannels: Boolean = true,
    val availableChats: List<ChatInfo> = listOf(),
    val selectedChatIds: List<Long> = listOf(),
    val close: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() {
            return queries.isNotEmpty() && (onlyChannels || selectedChats.isNotEmpty())
        }

    val selectedChats: List<ChatInfo> by lazy {
        availableChats.filter { it.id in selectedChatIds }
    }

}
package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import com.github.lucascalheiros.domain.model.ChatInfo

data class FilterSettingsUiState(
    val filterTitle: String = "",
    val queries: List<String> = listOf(),
    val allChannels: Boolean = true,
    val availableChats: List<ChatInfo> = listOf(),
    val selectedChats: List<ChatInfo> = listOf(),
    val close: Boolean = false,
    val showAddQueryDialog: Boolean = false,
    val showSaveDisabledHint: Boolean = false,
    val showSelectChatDialog: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() {
            return queries.isNotEmpty() && (allChannels || selectedChats.isNotEmpty())
        }
}
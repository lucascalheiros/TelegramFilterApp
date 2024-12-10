package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import com.github.lucascalheiros.domain.model.ChatInfo

data class FilterSettingsUiState(
    val filterTitle: String = "",
    val queries: List<String> = listOf(),
    val onlyChannels: Boolean = true,
    val availableChats: List<ChatInfo> = listOf(),
    val selectedChatIds: List<Long> = listOf(),
    val close: Boolean = false,
    val showAddQueryDialog: Boolean = false,
    val showSaveDisabledHint: Boolean = false,
    val showSelectChatDialog: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() {
            return queries.isNotEmpty() && (onlyChannels || selectedChats.isNotEmpty())
        }

    val selectedChats: List<ChatInfo> by lazy {
        availableChats.filter { it.id in selectedChatIds }
    }

}
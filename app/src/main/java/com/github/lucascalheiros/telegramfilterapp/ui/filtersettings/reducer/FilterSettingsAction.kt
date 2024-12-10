package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer

import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.Filter

sealed interface FilterSettingsAction {
    data class SetFilter(val filter: Filter): FilterSettingsAction
    data class UpdateTitle(val title: String): FilterSettingsAction
    data class SetAllChannelsState(val state: Boolean): FilterSettingsAction
    data object Close: FilterSettingsAction
    data class AddQuery(val text: String): FilterSettingsAction
    data object ShowAddQuery: FilterSettingsAction
    data object DismissAddQuery: FilterSettingsAction
    data class RemoveQuery(val index: Int): FilterSettingsAction
    data object ShowSelectChat: FilterSettingsAction
    data object DismissSelectChat: FilterSettingsAction
    data class UpdateAvailableChats(val chats: List<ChatInfo>): FilterSettingsAction
    data class SetSelectedChats(val chatIds: List<Long>): FilterSettingsAction
    data class RemoveChat(val chatId: Long): FilterSettingsAction
}
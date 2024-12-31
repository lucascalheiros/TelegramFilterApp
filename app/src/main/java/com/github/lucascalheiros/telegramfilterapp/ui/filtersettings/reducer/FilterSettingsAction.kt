package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer

import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterStrategy
import java.time.LocalDateTime

sealed interface FilterSettingsAction {
    data class SetFilter(val filter: Filter): FilterSettingsAction
    data class UpdateTitle(val title: String): FilterSettingsAction
    data object Close: FilterSettingsAction
    data class AddQuery(val text: String): FilterSettingsAction
    data class RemoveQuery(val index: Int): FilterSettingsAction
    data class UpdateAvailableChats(val chats: List<ChatInfo>): FilterSettingsAction
    data class AddSelectedChats(val chatIds: List<Long>): FilterSettingsAction
    data class RemoveChat(val chatId: Long): FilterSettingsAction
    data class SetFilterDateTime(val dateTime: LocalDateTime): FilterSettingsAction
    data class SetFilterStrategy(val strategy: FilterStrategy): FilterSettingsAction
    data class UpdateRegex(val regex: String): FilterSettingsAction
}
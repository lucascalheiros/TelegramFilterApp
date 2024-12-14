package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import java.time.LocalDateTime

sealed interface FilterSettingsIntent {
    data object LoadData: FilterSettingsIntent
    data class UpdateTitle(val value: String): FilterSettingsIntent
    data object Save: FilterSettingsIntent
    data class SetAllChannelsState(val state: Boolean): FilterSettingsIntent
    data class AddQuery(val text: String): FilterSettingsIntent
    data class RemoveQuery(val index: Int): FilterSettingsIntent
    data class SetSelectedChats(val chatIds: List<Long>): FilterSettingsIntent
    data class RemoveChat(val chatId: Long): FilterSettingsIntent
    data class SetFilterDateTime(val dateTime: LocalDateTime): FilterSettingsIntent
}
package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import com.github.lucascalheiros.domain.model.FilterType
import java.time.LocalDateTime

sealed interface FilterSettingsIntent {
    data object LoadData: FilterSettingsIntent
    data class UpdateTitle(val title: String): FilterSettingsIntent
    data class UpdateRegex(val regex: String): FilterSettingsIntent
    data object Save: FilterSettingsIntent
    data class AddQuery(val text: String): FilterSettingsIntent
    data class RemoveQuery(val index: Int): FilterSettingsIntent
    data class AddSelectedChats(val chatIds: List<Long>): FilterSettingsIntent
    data class RemoveChat(val chatId: Long): FilterSettingsIntent
    data class SetFilterDateTime(val dateTime: LocalDateTime): FilterSettingsIntent
    data class SetFilterStrategy(val strategy: FilterType): FilterSettingsIntent
    data class UpdateFuzzyDistance(val distance: Int): FilterSettingsIntent
}
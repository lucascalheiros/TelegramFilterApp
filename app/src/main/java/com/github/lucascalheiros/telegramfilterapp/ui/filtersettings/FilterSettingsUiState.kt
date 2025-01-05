package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.model.FilterType
import java.time.LocalDateTime

data class FilterSettingsUiState(
    val filterTitle: String = "",
    val filterDateTime: LocalDateTime = LocalDateTime.now(),
    val filterType: FilterType = FilterType.TelegramQuerySearch,
    val queries: List<String> = listOf(),
    val regex: String = "",
    val allAvailableChats: List<ChatInfo> = listOf(),
    val selectedChatIds: List<Long> = listOf(),
    val close: Boolean = false,
    val fuzzyDistance: Int = 1
) {

    val selectedChats: List<ChatInfo> by lazy {
        allAvailableChats.filter { it.id in selectedChatIds }
    }

    val chatsAvailableToSelect: List<ChatInfo> by lazy {
        allAvailableChats.filter { it.id !in selectedChatIds }
    }

    val hasQueriesOnFilterType: Boolean by lazy {
        when (filterType) {
            FilterType.TelegramQuerySearch,
            FilterType.LocalFuzzySearch -> true
            FilterType.LocalRegexSearch -> false
        }
    }

    val hasRegexOnFilterType: Boolean by lazy {
        when (filterType) {
            FilterType.TelegramQuerySearch,
            FilterType.LocalFuzzySearch -> false
            FilterType.LocalRegexSearch -> true
        }
    }

    val hasFuzzyDistanceOnFilterType: Boolean by lazy {
        when (filterType) {
            FilterType.TelegramQuerySearch,
            FilterType.LocalRegexSearch -> false
            FilterType.LocalFuzzySearch -> true
        }
    }
}
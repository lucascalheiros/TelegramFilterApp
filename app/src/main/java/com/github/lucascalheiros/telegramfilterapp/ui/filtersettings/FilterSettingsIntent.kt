package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

sealed interface FilterSettingsIntent {
    data object LoadData: FilterSettingsIntent
    data class UpdateTitle(val value: String): FilterSettingsIntent
    data object Save: FilterSettingsIntent
    data object ToggleAllChannels: FilterSettingsIntent
    data class AddQuery(val text: String): FilterSettingsIntent
    data object ShowAddQuery: FilterSettingsIntent
    data object DismissAddQuery: FilterSettingsIntent
    data class RemoveQuery(val index: Int): FilterSettingsIntent
    data object ShowSelectChat: FilterSettingsIntent
    data object DismissSelectChat: FilterSettingsIntent
}
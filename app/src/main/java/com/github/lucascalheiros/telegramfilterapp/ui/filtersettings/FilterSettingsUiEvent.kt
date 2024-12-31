package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

sealed interface FilterSettingsUiEvent {
    data object DataLoadingFailed: FilterSettingsUiEvent
}
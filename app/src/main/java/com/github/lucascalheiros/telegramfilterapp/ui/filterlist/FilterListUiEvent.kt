package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

sealed interface FilterListUiEvent {
    sealed interface DeleteFilter : FilterListUiEvent {
        data class Success(val filterName: String) : DeleteFilter
        data class Failure(val filterName: String) : DeleteFilter
    }

    data object DataLoadingFailure : FilterListUiEvent
}
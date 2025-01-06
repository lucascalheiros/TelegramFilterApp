package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

sealed interface FilterListIntent {
    data class DeleteFilter(
        val filterId: Long,
        val filterName: String
    ) : FilterListIntent

    data object Logout : FilterListIntent
}
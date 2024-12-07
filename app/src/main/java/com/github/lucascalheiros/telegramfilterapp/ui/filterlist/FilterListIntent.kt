package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

sealed interface FilterListIntent {
    data object LoadData: FilterListIntent
    data class DeleteFilter(val filterId: Long): FilterListIntent
}
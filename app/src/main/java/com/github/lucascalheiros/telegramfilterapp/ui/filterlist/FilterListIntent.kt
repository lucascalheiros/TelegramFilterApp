package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

sealed interface FilterListIntent {
    data object LoadData: FilterListIntent
}
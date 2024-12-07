package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer

import com.github.lucascalheiros.domain.model.Filter

sealed interface FilterListAction {
    data class SetFilters(val data: List<Filter>) : FilterListAction
}
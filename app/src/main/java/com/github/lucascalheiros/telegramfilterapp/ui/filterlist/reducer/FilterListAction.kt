package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer

import com.github.lucascalheiros.domain.model.Filter

sealed interface FilterListAction

sealed interface FilterLoadAction: FilterListAction {
    data class SetLoad(val loading: Boolean) : FilterLoadAction
    data class Success(val data: List<Filter>) : FilterLoadAction
}

sealed interface LogoutAction: FilterListAction {
    data object Handled: LogoutAction
    data object Loading: LogoutAction
    data object Failure: LogoutAction
    data object Success: LogoutAction
}

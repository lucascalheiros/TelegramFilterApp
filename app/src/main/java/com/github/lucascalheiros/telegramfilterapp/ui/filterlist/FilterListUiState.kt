package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import com.github.lucascalheiros.domain.model.Filter

data class FilterListUiState(
    val filterState: FilterState = FilterState.Idle,
    val logoutState: LogoutState = LogoutState.Idle,
) {
    val filters: List<Filter> by lazy {
        filterState.data
    }

    val isLogoutOnGoing by lazy {
        logoutState is LogoutState.Loading || logoutState is LogoutState.Success
    }
}

sealed interface FilterState {
    val data: List<Filter>
    data object Idle: FilterState {
        override val data: List<Filter> = listOf()
    }
    data class Loading(override val data: List<Filter>): FilterState
    data class Loaded(override val data: List<Filter>): FilterState
}

sealed interface LogoutState {
    data object Idle: LogoutState
    data object Loading: LogoutState
    data object Failure: LogoutState
    data object Success: LogoutState
}

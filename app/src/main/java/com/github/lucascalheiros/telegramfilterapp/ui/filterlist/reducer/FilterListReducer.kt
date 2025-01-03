package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer

import com.github.lucascalheiros.telegramfilterapp.ui.base.Reducer
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListUiState
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterState
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.LogoutState
import javax.inject.Inject

class FilterListReducer @Inject constructor() :
    Reducer<FilterListUiState, FilterListAction> {

    override fun reduce(
        state: FilterListUiState, action: FilterListAction
    ): FilterListUiState {
        return when (action) {
            LogoutAction.Failure -> state.copy(logoutState = LogoutState.Failure)
            LogoutAction.Handled -> state.copy(logoutState = LogoutState.Idle)
            LogoutAction.Loading -> state.copy(logoutState = LogoutState.Loading)
            LogoutAction.Success -> state.copy(logoutState = LogoutState.Success)
            is FilterLoadAction.SetLoad -> state.copy(
                filterState = if (action.loading) FilterState.Loading(state.filters)
                else FilterState.Loaded(state.filters)
            )

            is FilterLoadAction.Success -> state.copy(filterState = FilterState.Loaded(action.data))
        }
    }

}

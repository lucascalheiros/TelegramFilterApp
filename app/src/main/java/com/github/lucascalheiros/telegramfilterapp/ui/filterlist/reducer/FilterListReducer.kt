package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer

import com.github.lucascalheiros.telegramfilterapp.ui.Reducer
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListUiState
import javax.inject.Inject


class FilterListReducer @Inject constructor() :
    Reducer<FilterListUiState, FilterListAction> {

    override fun reduce(
        state: FilterListUiState, action: FilterListAction
    ): FilterListUiState {
        return when (action) {
            is FilterListAction.SetFilters -> state.copy(filters = action.data)
        }
    }

}




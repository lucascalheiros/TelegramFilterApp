package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.domain.usecases.DeleteFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListAction
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterListViewModel @Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    private val reducer: FilterListReducer,
    private val deleteFilterUseCase: DeleteFilterUseCase
): ViewModel() {

    private val _state = MutableStateFlow(FilterListUiState())
    val state = _state.asStateFlow()

    fun dispatch(intent: FilterListIntent) {
        viewModelScope.launch {
            val action = intentHandleMiddleware(intent)
            _state.update {
                reducer.reduce(it, action)
            }
        }
    }

    private suspend fun intentHandleMiddleware(intent: FilterListIntent): FilterListAction {
        return when (intent) {
            FilterListIntent.LoadData -> {
                val filters = getFilterUseCase.getFilters()
                FilterListAction.SetFilters(filters)
            }

            is FilterListIntent.DeleteFilter -> {
                deleteFilterUseCase(intent.filterId)
                val filters = getFilterUseCase.getFilters()
                FilterListAction.SetFilters(filters)
            }
        }
    }
}

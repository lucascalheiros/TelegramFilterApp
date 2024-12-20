package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.domain.usecases.DeleteFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.LogoutUseCase
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListAction
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListReducer
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterLoadAction
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.LogoutAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterListViewModel @Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    private val reducer: FilterListReducer,
    private val deleteFilterUseCase: DeleteFilterUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    private val _state = MutableStateFlow(FilterListUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<FilterListUiEvent>()
    val event = _event.asSharedFlow()

    fun dispatch(intent: FilterListIntent) {
        viewModelScope.launch {
            intentHandleMiddleware(intent).run(::reduceAction)
        }
    }

    private suspend fun intentHandleMiddleware(intent: FilterListIntent): FilterListAction {
        return when (intent) {
            FilterListIntent.LoadData -> handleLoadData()

            is FilterListIntent.DeleteFilter -> handle(intent)

            FilterListIntent.Logout -> handleLogout()
        }
    }

    private fun reduceAction(action: FilterListAction) {
        _state.update {
            reducer.reduce(it, action)
        }
    }

    private suspend fun sendEvent(event: FilterListUiEvent) {
        _event.emit(event)
    }

    private suspend fun handleLoadData(): FilterListAction {
        return try {
            reduceAction(FilterLoadAction.Loading)
            val filters = getFilterUseCase.getFilters()
            FilterLoadAction.Success(filters)
        } catch (e: Exception) {
            logError("::handleLoadData", e)
            FilterLoadAction.Failure
        }
    }

    private suspend fun handle(intent: FilterListIntent.DeleteFilter): FilterListAction {
        val event = try {
            deleteFilterUseCase(intent.filterId)
            FilterListUiEvent.DeleteFilter.Success(intent.filterName)
        } catch (e: Exception) {
            FilterListUiEvent.DeleteFilter.Failure(intent.filterName)
        }
        sendEvent(event)
        return handleLoadData()
    }

    private suspend fun handleLogout(): FilterListAction {
        reduceAction(LogoutAction.Loading)
        return try {
            logoutUseCase()
            LogoutAction.Success
        } catch (e: Exception) {
            logError("::handleLogout", e)
            LogoutAction.Failure
        }
    }
}

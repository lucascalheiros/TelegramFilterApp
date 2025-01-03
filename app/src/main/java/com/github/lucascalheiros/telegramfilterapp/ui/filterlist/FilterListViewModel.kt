package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.domain.usecases.DeleteFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.LogoutUseCase
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListAction
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListReducer
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterLoadAction
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.LogoutAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
    private val logoutUseCase: LogoutUseCase,
    private val analyticsReporter: AnalyticsReporter
): ViewModel() {

    private val _state = MutableStateFlow(FilterListUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<FilterListUiEvent>()
    val event = _event.asSharedFlow()

    fun dispatch(intent: FilterListIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            analyticsReporter.addNonFatalReport(throwable)
        }) {
            intentHandleMiddleware(intent)
        }
    }

    private suspend fun intentHandleMiddleware(intent: FilterListIntent) {
        when (intent) {
            FilterListIntent.LoadData -> loadData()

            is FilterListIntent.DeleteFilter -> delete(intent.filterId, intent.filterName)

            FilterListIntent.Logout -> logout()
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

    private suspend fun loadData() {
        return try {
            reduceAction(FilterLoadAction.SetLoad(true))
            val filters = getFilterUseCase.getFilters()
            reduceAction(FilterLoadAction.Success(filters))
        } catch (e: Exception) {
            logError("::handleLoadData", e)
            analyticsReporter.addNonFatalReport(e)
            reduceAction(FilterLoadAction.SetLoad(false))
            sendEvent(FilterListUiEvent.DataLoadingFailure)
        }
    }

    private suspend fun delete(filterId: Long, filterName: String) {
        val event = try {
            deleteFilterUseCase(filterId)
            FilterListUiEvent.DeleteFilter.Success(filterName)
        } catch (e: Exception) {
            FilterListUiEvent.DeleteFilter.Failure(filterName)
        }
        sendEvent(event)
        loadData()
    }

    private suspend fun logout() {
        reduceAction(LogoutAction.Loading)
        val resultAction = try {
            logoutUseCase()
            LogoutAction.Success
        } catch (e: Exception) {
            logError("::handleLogout", e)
            analyticsReporter.addNonFatalReport(e)
            LogoutAction.Failure
        }
        reduceAction(resultAction)
    }
}

package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.domain.usecases.DeleteFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetMessagesUseCase
import com.github.lucascalheiros.domain.usecases.MarkFilterMessagesAsReadUseCase
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer.FilterMessagesAction
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer.FilterMessagesReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterMessagesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reducer: FilterMessagesReducer,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getFilterUseCase: GetFilterUseCase,
    private val analyticsReporter: AnalyticsReporter,
    private val deleteFilterUseCase: DeleteFilterUseCase,
    private val markFilterMessagesAsReadUseCase: MarkFilterMessagesAsReadUseCase
) : ViewModel() {

    private val filterSettingsParam = savedStateHandle.toRoute<NavRoute.FilterSettings>()
    private val filterId: Long
        get() = filterSettingsParam.filterId ?: 0

    private val _state = MutableStateFlow(FilterMessagesUiState())
    val state = _state.asStateFlow()

    fun dispatch(intent: FilterMessagesIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            analyticsReporter.addNonFatalReport(throwable)
        }) {
            intentHandleMiddleware(intent)
        }
    }

    private suspend fun intentHandleMiddleware(intent: FilterMessagesIntent) {
        when (intent) {
            FilterMessagesIntent.LoadData -> loadData()
            FilterMessagesIntent.DeleteFilter -> deleteFilter()
        }
    }

    private fun reduceAction(action: FilterMessagesAction) {
        _state.update {
            reducer.reduce(it, action)
        }
    }

    private suspend fun loadData() {
        markFilterMessagesAsReadUseCase(filterId)
        reduceAction(FilterMessagesAction.LoadingMessage)
        val filter = getFilterUseCase.getFilter(filterId) ?: return
        reduceAction(FilterMessagesAction.SetFilter(filter))
        val messages = getMessagesUseCase(filter)
        reduceAction(FilterMessagesAction.SetMessages(messages))
    }

    private suspend fun deleteFilter() {
        try {
            deleteFilterUseCase(filterId)
        } catch (_: Exception) {
        }
        reduceAction(FilterMessagesAction.Close)
    }
}
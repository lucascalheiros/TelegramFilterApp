package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.common.datetime.toMillis
import com.github.lucascalheiros.common.log.logDebug
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.usecases.GetChatsUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.SaveFilterUseCase
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.helpers.GetDefaultFilterNameHelper
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterSettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reducer: FilterSettingsReducer,
    private val getFilterUseCase: GetFilterUseCase,
    private val saveFilterUseCase: SaveFilterUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val getDefaultFilterNameHelper: GetDefaultFilterNameHelper,
    private val analyticsReporter: AnalyticsReporter,
) : ViewModel() {

    private val filterSettingsParam = savedStateHandle.toRoute<NavRoute.FilterSettings>()
    private val filterId: Long
        get() = filterSettingsParam.filterId ?: 0

    private val _state =
        MutableStateFlow(FilterSettingsUiState(filterTitle = getDefaultFilterNameHelper()))
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<FilterSettingsUiEvent>()
    val events = _events.asSharedFlow()

    suspend fun collectWithLifecycleScope() = coroutineScope {
        launch {
            getChatsUseCase().collectLatest {
                reduceAction(FilterSettingsAction.UpdateAvailableChats(it))
            }
        }
    }

    fun dispatch(intent: FilterSettingsIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            analyticsReporter.addNonFatalReport(throwable)
        }) {
            intentHandleMiddleware(intent)
        }
    }

    private suspend fun sendUiEvent(event: FilterSettingsUiEvent) {
        _events.emit(event)
    }

    private suspend fun intentHandleMiddleware(intent: FilterSettingsIntent) {
        logDebug("::intentHandleMiddleware $intent")
        val possibleAction: Any = when (intent) {
            FilterSettingsIntent.LoadData -> loadData()
            FilterSettingsIntent.Save -> save()
            is FilterSettingsIntent.UpdateTitle ->
                FilterSettingsAction.UpdateTitle(intent.title.takeIf { it.isNotBlank() }
                    ?: getDefaultFilterNameHelper())

            is FilterSettingsIntent.AddQuery ->
                FilterSettingsAction.AddQuery(intent.text)

            is FilterSettingsIntent.RemoveQuery ->
                FilterSettingsAction.RemoveQuery(intent.index)

            is FilterSettingsIntent.AddSelectedChats ->
                FilterSettingsAction.AddSelectedChats(intent.chatIds)

            is FilterSettingsIntent.RemoveChat ->
                FilterSettingsAction.RemoveChat(intent.chatId)

            is FilterSettingsIntent.SetFilterDateTime ->
                FilterSettingsAction.SetFilterDateTime(intent.dateTime)

            is FilterSettingsIntent.SetFilterStrategy ->
                FilterSettingsAction.SetFilterStrategy(intent.strategy)

            is FilterSettingsIntent.UpdateRegex ->
                FilterSettingsAction.UpdateRegex(intent.regex)

            is FilterSettingsIntent.UpdateFuzzyDistance ->
                FilterSettingsAction.UpdateFuzzyDistance(intent.distance)
        }
        if (possibleAction is FilterSettingsAction) {
            reduceAction(possibleAction)
        }
    }

    private fun reduceAction(action: FilterSettingsAction) {
        _state.update {
            reducer.reduce(it, action)
        }
    }

    private suspend fun loadFilter(): Filter? {
        val filterId = filterSettingsParam.filterId ?: return null
        return getFilterUseCase.invoke(filterId)
    }

    private suspend fun save() {
        return try {
            val state = state.value
            saveFilterUseCase(
                Filter(
                    id = filterId,
                    title = state.filterTitle,
                    queries = state.queries,
                    regex = state.regex,
                    chatIds = state.selectedChats.map { it.id },
                    limitDate = state.filterDateTime.toMillis(),
                    filterType = state.filterType,
                    fuzzyDistance = state.fuzzyDistance
                )
            )
            reduceAction(FilterSettingsAction.Close)
        } catch (e: Exception) {
            analyticsReporter.addNonFatalReport(e)
        }
    }

    private suspend fun loadData() {
        loadChats()
        return try {
            val filter = loadFilter() ?: return
            reduceAction(FilterSettingsAction.SetFilter(filter))
        } catch (e: Exception) {
            sendUiEvent(FilterSettingsUiEvent.DataLoadingFailed)
            reduceAction(FilterSettingsAction.Close)
        }
    }

    private fun loadChats() = viewModelScope.launch {
        try {
            getChatsUseCase.update()
        } catch (_: Exception) {
        }
    }
}


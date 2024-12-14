package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.lucascalheiros.common.datetime.toMillis
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.usecases.GetChatsUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.SaveFilterUseCase
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsAction
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer.FilterSettingsReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val getChatsUseCase: GetChatsUseCase
) : ViewModel() {

    private val filterSettingsParam = savedStateHandle.toRoute<NavRoute.FilterSettings>()
    private val filterId: Long
        get() = filterSettingsParam.filterId ?: 0

    private val _state = MutableStateFlow(FilterSettingsUiState())
    val state = _state.asStateFlow()

    private var watchChatsJob: Job? = null

    fun dispatch(intent: FilterSettingsIntent) {
        viewModelScope.launch {
            intentHandleMiddleware(intent)?.run(::reduceAction)
        }
    }

    private suspend fun intentHandleMiddleware(intent: FilterSettingsIntent): FilterSettingsAction? {
        return when (intent) {
            FilterSettingsIntent.LoadData -> {
                getChatsUseCase.update()
                if (watchChatsJob?.isActive != true) {
                    watchChatsJob = viewModelScope.watchChatUpdate()
                }

                val filter = loadFilter() ?: return null
                FilterSettingsAction.SetFilter(filter)
            }

            FilterSettingsIntent.Save -> {
                save()
                FilterSettingsAction.Close
            }
            is FilterSettingsIntent.SetAllChannelsState -> FilterSettingsAction.SetAllChannelsState(intent.state)
            is FilterSettingsIntent.UpdateTitle -> FilterSettingsAction.UpdateTitle(intent.value)
            is FilterSettingsIntent.AddQuery -> FilterSettingsAction.AddQuery(intent.text)
            is FilterSettingsIntent.RemoveQuery -> FilterSettingsAction.RemoveQuery(intent.index)
            is FilterSettingsIntent.SetSelectedChats -> FilterSettingsAction.SetSelectedChats(intent.chatIds)
            is FilterSettingsIntent.RemoveChat -> FilterSettingsAction.RemoveChat(intent.chatId)
            is FilterSettingsIntent.SetFilterDateTime -> FilterSettingsAction.SetFilterDateTime(intent.dateTime)
        }
    }

    private fun reduceAction(action: FilterSettingsAction) {
        _state.update {
            reducer.reduce(it, action)
        }
    }

    private suspend fun loadFilter(): Filter? {
        val filterId = filterSettingsParam.filterId ?: return null
        return getFilterUseCase.getFilter(filterId)
    }

    private suspend fun save() {
        val state = state.value
        saveFilterUseCase(
            Filter(
                id = filterId,
                title = state.filterTitle,
                queries = state.queries,
                onlyChannels = state.onlyChannels,
                chatIds = state.selectedChats.map { it.id },
                dateLimit = state.filterDateTime.toMillis()
            )
        )
    }

    private fun CoroutineScope.watchChatUpdate() = launch {
        getChatsUseCase().collectLatest {
            reduceAction(FilterSettingsAction.UpdateAvailableChats(it))
        }
    }
}


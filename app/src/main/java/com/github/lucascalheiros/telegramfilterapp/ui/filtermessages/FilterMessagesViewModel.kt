package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetMessagesUseCase
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer.FilterMessagesAction
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer.FilterMessagesReducer
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getFilterUseCase: GetFilterUseCase
) : ViewModel() {

    private val filterSettingsParam = savedStateHandle.toRoute<NavRoute.FilterSettings>()
    private val filterId: Long
        get() = filterSettingsParam.filterId ?: 0

    private val _state = MutableStateFlow(FilterMessagesUiState())
    val state = _state.asStateFlow()

    fun dispatch(intent: FilterMessagesIntent) {
        viewModelScope.launch {
            intentHandleMiddleware(intent)?.run(::reduceAction)
        }
    }

    private suspend fun intentHandleMiddleware(intent: FilterMessagesIntent): FilterMessagesAction? {
        return when (intent) {
            FilterMessagesIntent.LoadData -> {
                reduceAction(FilterMessagesAction.LoadingMessage)
                val filter = getFilterUseCase.getFilter(filterId) ?: return null
                val messages = getMessagesUseCase(filter)
                FilterMessagesAction.SetMessages(messages)
            }
        }
    }

    private fun reduceAction(action: FilterMessagesAction) {
        _state.update {
            reducer.reduce(it, action)
        }
    }
}
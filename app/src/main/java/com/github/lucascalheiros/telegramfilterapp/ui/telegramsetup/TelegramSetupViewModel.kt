package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.domain.usecases.GetAuthorizationStepUseCase
import com.github.lucascalheiros.domain.usecases.SendSetupCodeUseCase
import com.github.lucascalheiros.domain.usecases.SendSetupNumberUseCase
import com.github.lucascalheiros.domain.usecases.SendSetupPasswordUseCase
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.reducer.TelegramSetupAction
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.reducer.TelegramSetupReducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TelegramSetupViewModel @Inject constructor(
    private val getAuthorizationStepUseCase: GetAuthorizationStepUseCase,
    private val sendSetupNumberUseCase: SendSetupNumberUseCase,
    private val sendSetupCodeUseCase: SendSetupCodeUseCase,
    private val sendSetupPasswordUseCase: SendSetupPasswordUseCase,
    private val reducer: TelegramSetupReducer
) : ViewModel() {

    private val _state = MutableStateFlow(TelegramSetupUiState())
    val state = _state.asStateFlow()

    fun dispatch(intent: TelegramSetupIntent) {
        viewModelScope.launch {
            intentHandleMiddleware(intent)?.run(this@TelegramSetupViewModel::reduceAction)
        }
    }

    private suspend fun intentHandleMiddleware(intent: TelegramSetupIntent): TelegramSetupAction? {
        return when(intent) {
            TelegramSetupIntent.NextStep -> {
                nextStep()
                null
            }
            is TelegramSetupIntent.UpdateCode -> TelegramSetupAction.UpdateCode(intent.value)
            is TelegramSetupIntent.UpdatePassword -> TelegramSetupAction.UpdatePassword(intent.value)
            is TelegramSetupIntent.UpdatePhoneNumber -> TelegramSetupAction.UpdatePhoneNumber(intent.value)
        }
    }

    private fun reduceAction(action: TelegramSetupAction) {
        _state.update {
            reducer.reduce(it, action)
        }
    }

    suspend fun watchStateUpdate() = coroutineScope {
        getAuthorizationStepUseCase().filterNotNull().collectLatest { step ->
            reduceAction(TelegramSetupAction.UpdateStep(step))
        }
    }

    private suspend fun nextStep() {
        val state = state.value
        if (!state.isActionEnabled) {
            return
        }
        reduceAction(TelegramSetupAction.SetStepLoadingState(true))
        when (state.step) {
            AuthorizationStep.PhoneInput -> sendSetupNumberUseCase(state.phoneNumber)

            AuthorizationStep.CodeInput -> sendSetupCodeUseCase(state.code)

            AuthorizationStep.PasswordInput -> sendSetupPasswordUseCase(state.password)

            else -> return
        }
    }

}


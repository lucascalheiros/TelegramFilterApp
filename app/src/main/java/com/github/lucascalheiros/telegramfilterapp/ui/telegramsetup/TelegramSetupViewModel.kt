package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.domain.usecases.GetAuthorizationStepUseCase
import com.github.lucascalheiros.domain.usecases.LogoutUseCase
import com.github.lucascalheiros.domain.usecases.SendSetupCodeUseCase
import com.github.lucascalheiros.domain.usecases.SendSetupNumberUseCase
import com.github.lucascalheiros.domain.usecases.SendSetupPasswordUseCase
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.reducer.TelegramSetupAction
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.reducer.TelegramSetupReducer
import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val reducer: TelegramSetupReducer,
    private val logoutUseCase: LogoutUseCase,
    private val analyticsReporter: AnalyticsReporter,
) : ViewModel() {

    private val _state = MutableStateFlow(TelegramSetupUiState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<TelegramSetupUiEvent>()
    val event = _event.asSharedFlow()

    fun dispatch(intent: TelegramSetupIntent) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            analyticsReporter.addNonFatalReport(throwable)
        }) {
            intentHandleMiddleware(intent)
        }
    }

    private suspend fun sendEvent(event: TelegramSetupUiEvent) {
        _event.emit(event)
    }

    private suspend fun intentHandleMiddleware(intent: TelegramSetupIntent) {
        val possibleAction: Any = when (intent) {
            TelegramSetupIntent.NextStep -> nextStep()

            is TelegramSetupIntent.UpdateCode -> TelegramSetupAction.UpdateCode(intent.value)

            is TelegramSetupIntent.UpdatePassword -> TelegramSetupAction.UpdatePassword(intent.value)

            is TelegramSetupIntent.UpdatePhoneNumber -> TelegramSetupAction.UpdatePhoneNumber(intent.value)

            TelegramSetupIntent.ChangeNumber -> TelegramSetupAction.UpdateStep(AuthorizationStep.PhoneInput)

            TelegramSetupIntent.CancelSetup -> cancelSetup()
        }
        if (possibleAction is TelegramSetupAction) {
            reduceAction(possibleAction)
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

    private suspend fun cancelSetup() {
        return try {
            reduceAction(TelegramSetupAction.SetStepLoadingState(true))
            logoutUseCase()
        } catch (_: Exception) {
        }
    }

    private suspend fun nextStep() {
        val state = state.value
        if (!state.isActionEnabled) {
            return
        }
        reduceAction(TelegramSetupAction.SetStepLoadingState(true))
        when (state.step) {
            AuthorizationStep.PhoneInput ->
                try {
                    sendSetupNumberUseCase(state.phoneNumber)
                    reduceAction(TelegramSetupAction.UpdatePhoneNumber(""))
                } catch (e: Exception) {
                    sendEvent(TelegramSetupUiEvent.SendPhoneNumberFailedToast)
                    reduceAction(TelegramSetupAction.SetStepLoadingState(false))
                }

            is AuthorizationStep.CodeInput ->
                try {
                    sendSetupCodeUseCase(state.code)
                    reduceAction(TelegramSetupAction.UpdateCode(""))
                } catch (e: Exception) {
                    sendEvent(TelegramSetupUiEvent.SendAuthCodeFailedToast)
                    reduceAction(TelegramSetupAction.SetStepLoadingState(false))
                }

            AuthorizationStep.PasswordInput ->
                try {
                    sendSetupPasswordUseCase(state.password)
                    reduceAction(TelegramSetupAction.UpdatePassword(""))
                } catch (e: Exception) {
                    sendEvent(TelegramSetupUiEvent.SendPasswordFailedToast)
                    reduceAction(TelegramSetupAction.SetStepLoadingState(false))
                }

            else -> return
        }
    }

}


package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.reducer

import com.github.lucascalheiros.telegramfilterapp.ui.base.Reducer
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.TelegramSetupUiState
import javax.inject.Inject

class TelegramSetupReducer @Inject constructor():
    Reducer<TelegramSetupUiState, TelegramSetupAction> {
    override fun reduce(
        state: TelegramSetupUiState,
        action: TelegramSetupAction
    ): TelegramSetupUiState {
        return when (action) {
            is TelegramSetupAction.UpdateCode -> state.copy(code = action.code)
            is TelegramSetupAction.UpdatePassword -> state.copy(password = action.password)
            is TelegramSetupAction.UpdatePhoneNumber -> state.copy(phoneNumber = action.phoneNumber)
            is TelegramSetupAction.UpdateStep -> state.copy(step = action.step, isStepLoading = false)
            is TelegramSetupAction.SetStepLoadingState -> state.copy(isStepLoading = action.isStepLoading)
        }
    }
}
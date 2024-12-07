package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.reducer

import com.github.lucascalheiros.domain.model.AuthorizationStep

sealed interface TelegramSetupAction {
    data class UpdatePhoneNumber(val phoneNumber: String): TelegramSetupAction
    data class UpdateCode(val code: String): TelegramSetupAction
    data class UpdatePassword(val password: String): TelegramSetupAction
    data class UpdateStep(val step: AuthorizationStep): TelegramSetupAction
    data class SetStepLoadingState(val isStepLoading: Boolean): TelegramSetupAction
}
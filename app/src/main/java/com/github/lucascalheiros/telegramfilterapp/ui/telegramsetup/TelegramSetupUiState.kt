package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import com.github.lucascalheiros.domain.model.AuthorizationStep

data class TelegramSetupUiState(
    val phoneNumber: String = "",
    val code: String = "",
    val password: String = "",
    val step: AuthorizationStep? = null,
    val isStepLoading: Boolean = true
) {
    val isActionEnabled: Boolean by lazy {
        step == AuthorizationStep.PhoneInput && phoneNumber.isNotBlank() ||
                step == AuthorizationStep.CodeInput && code.length == 5 ||
                step == AuthorizationStep.PasswordInput && password.isNotEmpty()

    }
}
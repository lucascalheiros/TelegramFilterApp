package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

data class TelegramSetupUiState(
    val phoneNumber: String = "",
    val areaCode: String = getCurrentAreaCode(),
    val code: String = "",
    val password: String = "",
    val step: AuthorizationStep? = null,
    val isStepLoading: Boolean = true
) {
    val isActionEnabled: Boolean by lazy {
        step == AuthorizationStep.PhoneInput && isPhoneNumberValid ||
                step == AuthorizationStep.CodeInput && code.length == 5 ||
                step == AuthorizationStep.PasswordInput && password.isNotEmpty()
    }

    val isPhoneNumberValid: Boolean by lazy {
        val phoneUtil = PhoneNumberUtil.getInstance()
        phoneUtil.isValidNumber(phoneUtil.parse("+$areaCode$phoneNumber", null))
    }

    val isPhoneInputVisible: Boolean by lazy {
        !isStepLoading && step == AuthorizationStep.PhoneInput
    }

    val isCodeInputVisible: Boolean by lazy {
        !isStepLoading && step == AuthorizationStep.CodeInput
    }

    val isPasswordInputVisible: Boolean by lazy {
        !isStepLoading && step == AuthorizationStep.PasswordInput
    }
}

fun getCurrentAreaCode(): String {
    val phoneUtil = PhoneNumberUtil.getInstance()
    return phoneUtil.getCountryCodeForRegion(Locale.getDefault().country).toString()
}
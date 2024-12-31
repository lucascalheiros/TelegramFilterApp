package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

data class TelegramSetupUiState(
    val phoneNumber: String = getCurrentAreaCode(),
    val code: String = "",
    val password: String = "",
    val step: AuthorizationStep? = null,
    val isStepLoading: Boolean = true
) {
    val isActionEnabled: Boolean by lazy {
        step == AuthorizationStep.PhoneInput && isPhoneNumberValid ||
                step is AuthorizationStep.CodeInput && code.length == 5 ||
                step == AuthorizationStep.PasswordInput && password.isNotEmpty()
    }

    val isPhoneNumberValid: Boolean by lazy {
        val phoneUtil = PhoneNumberUtil.getInstance()
        try {
            phoneUtil.isValidNumber(phoneUtil.parse("+$phoneNumber", null))
        } catch (e: Exception) {
            false
        }
    }

    val displayCountry: String by lazy {
        val phoneUtil = PhoneNumberUtil.getInstance()
        try {
            val number = phoneUtil.parse("+$phoneNumber", null)
            val region = phoneUtil.getRegionCodeForNumber(number)
            Locale("", region).displayCountry
        } catch (e: Exception) {
            ""
        }
    }

    val isPhoneInputVisible: Boolean by lazy {
        !isStepLoading && step == AuthorizationStep.PhoneInput
    }

    val isCodeInputVisible: Boolean by lazy {
        !isStepLoading && step is AuthorizationStep.CodeInput
    }

    val isPasswordInputVisible: Boolean by lazy {
        !isStepLoading && step == AuthorizationStep.PasswordInput
    }

    companion object {
        fun getCurrentAreaCode(): String {
            val phoneUtil = PhoneNumberUtil.getInstance()
            return phoneUtil.getCountryCodeForRegion(Locale.getDefault().country).toString()
        }
    }
}

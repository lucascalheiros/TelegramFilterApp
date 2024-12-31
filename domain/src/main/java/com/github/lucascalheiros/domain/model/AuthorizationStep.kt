package com.github.lucascalheiros.domain.model

sealed interface AuthorizationStep {
    data object PhoneInput: AuthorizationStep
    data class CodeInput(val phoneNumber: String): AuthorizationStep
    data object PasswordInput: AuthorizationStep
    data object Ready: AuthorizationStep
}
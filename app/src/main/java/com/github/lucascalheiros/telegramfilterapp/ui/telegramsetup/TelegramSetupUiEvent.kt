package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

sealed interface TelegramSetupUiEvent {
    data object SendPasswordFailedToast: TelegramSetupUiEvent
    data object SendAuthCodeFailedToast: TelegramSetupUiEvent
    data object SendPhoneNumberFailedToast: TelegramSetupUiEvent
}
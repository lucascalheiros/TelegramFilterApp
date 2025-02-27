package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

sealed interface TelegramSetupIntent {
    data class UpdatePhoneNumber(val value: String): TelegramSetupIntent
    data class UpdateCode(val value: String): TelegramSetupIntent
    data class UpdatePassword(val value: String): TelegramSetupIntent
    data object NextStep: TelegramSetupIntent
    data object ChangeNumber: TelegramSetupIntent
    data object CancelSetup: TelegramSetupIntent
}
package com.github.lucascalheiros.telegramfilterapp.notification

import com.github.lucascalheiros.common.log.logDebug
import com.github.lucascalheiros.domain.usecases.SetupTelegramUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Service complying with td lib architecture, notification will be received by TdApi.UpdateNewMessage
 * rather than directly from onMessageReceived please check [NewNotificationChannelConsumer] if you
 * want to check the notification will be triggered.
 * The firebase messaging service will only ensure that the TdLib is properly initialized.
 */
@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var setupTelegramUseCase: SetupTelegramUseCase

    override fun onNewToken(token: String) {
        logDebug("::onNewToken $token")
        setupTelegramUseCase.updatePNToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        logDebug(message.toString())
        setupTelegramUseCase()
    }
}
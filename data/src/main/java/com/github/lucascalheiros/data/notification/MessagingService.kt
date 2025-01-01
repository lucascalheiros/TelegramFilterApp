package com.github.lucascalheiros.data.notification

import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.common.log.logDebug
import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var telegramClientWrapper: TelegramClientWrapper

    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    override fun onNewToken(token: String) {
        logDebug("::onNewToken $token")
        telegramClientWrapper.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        logDebug(message.toString())
        telegramClientWrapper.setup()
    }
}
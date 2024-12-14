package com.github.lucascalheiros.telegramfilterapp.notification

import com.github.lucascalheiros.common.log.logDebug
import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.RegisterDevice
import javax.inject.Inject

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var telegramClientWrapper: TelegramClientWrapper

    override fun onNewToken(token: String) {
        logDebug("::onNewToken $token")

        runBlocking {
            telegramClientWrapper.send(
                RegisterDevice(
                    TdApi.DeviceTokenFirebaseCloudMessaging(token, true),
                    longArrayOf()
                )
            )
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        logDebug(message.toString())
        telegramClientWrapper.setup()
    }
}
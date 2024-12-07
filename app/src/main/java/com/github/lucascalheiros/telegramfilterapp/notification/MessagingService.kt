package com.github.lucascalheiros.telegramfilterapp.notification

import android.util.Log
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
        Log.d("MessagingService", "::onNewToken $token")

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
        Log.d("MessagingService", message.toString())
        telegramClientWrapper.setup()
    }
}
package com.github.lucascalheiros.data.frameworks.telegram

import android.content.Context
import com.github.lucascalheiros.common.di.IoDispatcher
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.RegisterDevice
import javax.inject.Inject
import javax.inject.Singleton
import com.github.lucascalheiros.common.log.logDebug
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.data.notification.NotificationFilterHandler

@Singleton
class TelegramClientWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val notificationFilterHandler: NotificationFilterHandler
) {

    private var telegramClient: Client? = null

    private val _currentAuthState = MutableStateFlow<TdApi.UpdateAuthorizationState?>(null)
    val currentAuthState = _currentAuthState.asStateFlow()

    private val _chats = MutableStateFlow<Map<Long, TdApi.Chat>>(mapOf())
    val chats = _chats.asStateFlow()

    fun getChat(id: Long): TdApi.Chat? {
        return chats.value[id]
    }

    fun setup() {
        if (telegramClient != null) {
            return
        }
        telegramClient = Client.create(
            { state ->
                logDebug(state.toString())

                when (state) {
                    is TdApi.UpdateAuthorizationState -> handle(state)

                    is TdApi.UpdateNewChat -> handle(state)

                    is TdApi.UpdateNewMessage -> handle(state)
                }
            },
            {
                logError("updateExceptionHandler", it)
            },
            {
                logError("defaultExceptionHandler", it)
            }
        )
    }

    suspend fun <T : TdApi.Object> send(query: TdApi.Function<T>): T {
        if (telegramClient == null) {
            setup()
        }
        return telegramClient!!.send(query)
    }

    private fun handle(state: TdApi.UpdateAuthorizationState) {
        _currentAuthState.value = state

        when (state.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> CoroutineScope(ioDispatcher).launch {
                send(setTdlibParameters(context))
            }

            is TdApi.AuthorizationStateReady -> CoroutineScope(ioDispatcher).launch {
                val token = FirebaseMessaging.getInstance().token.await()
                val deviceTokenFirebaseCloudMessaging = RegisterDevice(
                    TdApi.DeviceTokenFirebaseCloudMessaging(token, false),
                    longArrayOf()
                )
                send(deviceTokenFirebaseCloudMessaging)
            }

            is TdApi.AuthorizationStateClosed -> {
                telegramClient = null
                setup()
            }
        }
    }

    private fun handle(state: TdApi.UpdateNewMessage) {
        val message = state.message
        val chat = getChat(message.chatId)
        if (chat == null) {
            logDebug("chat not found from message $message")
            return
        }
        if (message.isOutgoing) {
            logDebug("message outgoing $message")
            return
        }
        logDebug("message received, proceeding to filter $message")
        notificationFilterHandler.handleNotification(message, chat)
    }

    private fun handle(state: TdApi.UpdateNewChat) {
        _chats.update { map ->
            map.toMutableMap().also {
                it[state.chat.id] = state.chat
            }
        }
    }
}

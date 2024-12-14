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

@Singleton
class TelegramClientWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val notificationHandler: NotificationHandler
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
                logDebug("updateExceptionHandler", it)
            },
            {
                logDebug("defaultExceptionHandler", it)
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
        }
    }

    private fun handle(state: TdApi.UpdateNewMessage) {
        val messageId = state.message.id
        val chatId = state.message.chatId
        val message = state.message
        if (message.isOutgoing) {
            return
        }
        logDebug("newMessageReceived $message")
        val title = getChat(message.chatId)?.title ?: ""
        val content = message.content.textContent()
        notificationHandler.handleNotification(
            chatId,
            messageId,
            title,
            content,
            message.isChannelPost
        )
    }

    private fun handle(state: TdApi.UpdateNewChat) {
        _chats.update { map ->
            map.toMutableMap().also {
                it[state.chat.id] = state.chat
            }
        }
    }
}

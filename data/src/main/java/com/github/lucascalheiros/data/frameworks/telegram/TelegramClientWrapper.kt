package com.github.lucascalheiros.data.frameworks.telegram

import android.content.Context
import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.common.log.logDebug
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.domain.model.Message
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
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

@Singleton
class TelegramClientWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val analyticsReporter: AnalyticsReporter
) {

    private var telegramClient: Client? = null

    private val _currentAuthState = MutableStateFlow<TdApi.UpdateAuthorizationState?>(null)
    val currentAuthState = _currentAuthState.asStateFlow()

    private val _chats = MutableStateFlow<Map<Long, TdApi.Chat>>(mapOf())
    val chats = _chats.asStateFlow()

    val newMessagesChannel = Channel<Message>(200, onBufferOverflow = BufferOverflow.DROP_OLDEST)

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

    fun updatePnToken(token: String? = null) {
        setup()
        CoroutineScope(ioDispatcher).launch(
            CoroutineExceptionHandler { _, throwable ->
                analyticsReporter.addNonFatalReport(throwable)
            }
        ) {
            val pnToken = token ?: FirebaseMessaging.getInstance().token.await()
            val deviceTokenFirebaseCloudMessaging = RegisterDevice(
                TdApi.DeviceTokenFirebaseCloudMessaging(pnToken, false),
                longArrayOf()
            )
            send(deviceTokenFirebaseCloudMessaging)
        }
    }

    private fun handle(state: TdApi.UpdateAuthorizationState) {
        _currentAuthState.value = state

        when (state.authorizationState) {
            is TdApi.AuthorizationStateWaitTdlibParameters -> CoroutineScope(ioDispatcher).launch(
                CoroutineExceptionHandler { _, throwable ->
                    analyticsReporter.addNonFatalReport(throwable)
                }
            ) {
                send(setTdlibParameters(context))
            }

            is TdApi.AuthorizationStateReady -> updatePnToken()

            is TdApi.AuthorizationStateClosed -> {
                telegramClient = null
                setup()
            }
        }
    }

    private fun handle(state: TdApi.UpdateNewMessage) {
        val messageTd = state.message
        val chat = getChat(messageTd.chatId)
        if (chat == null) {
            logDebug("chat not found from message $messageTd")
            return
        }
        if (messageTd.isOutgoing) {
            logDebug("message outgoing $messageTd")
            return
        }
        logDebug("message received, proceeding to filter $messageTd")
        val message = Message(
            messageTd.id,
            messageTd.content.textContent(),
            messageTd.date,
            chat.title,
            messageTd.chatId
        )
        CoroutineScope(ioDispatcher).launch {
            newMessagesChannel.send(message)
        }
    }

    private fun handle(state: TdApi.UpdateNewChat) {
        _chats.update { map ->
            map.toMutableMap().also {
                it[state.chat.id] = state.chat
            }
        }
    }
}

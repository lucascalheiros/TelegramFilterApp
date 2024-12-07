package com.github.lucascalheiros.data.frameworks.telegram

import android.content.Context
import android.util.Log
import com.github.lucascalheiros.common.di.MainDispatcher
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

@Singleton
class TelegramClientWrapper @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
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
                Log.d(TAG, state.toString())

                when (state) {
                    is TdApi.UpdateAuthorizationState -> handle(state)

                    is TdApi.UpdateNewChat -> handle(state)

                    is TdApi.UpdateNewMessage -> handle(state)
                }
            },
            {
                Log.e(TAG, "updateExceptionHandler", it)
            },
            {
                Log.e(TAG, "defaultExceptionHandler", it)
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
            is TdApi.AuthorizationStateWaitTdlibParameters -> CoroutineScope(dispatcher).launch {
                send(setTdlibParameters(context))
            }

            is TdApi.AuthorizationStateReady -> CoroutineScope(dispatcher).launch {
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
        val message = state.message
        if (message.isOutgoing) {
            return
        }
        Log.d(TAG, "newMessageReceived $message")
        val title = getChat(message.chatId)?.title ?: ""
        val content = message.content.textContent()
        sendNotification(context, messageId, title, content)
    }

    private fun handle(state: TdApi.UpdateNewChat) {
        _chats.update { map ->
            map.toMutableMap().also {
                it[state.chat.id] = state.chat
            }
        }
    }

    companion object {
        private val TAG = TelegramClientWrapper::class.simpleName
    }
}

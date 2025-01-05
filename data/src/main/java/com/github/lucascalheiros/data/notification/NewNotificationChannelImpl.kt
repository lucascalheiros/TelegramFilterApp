package com.github.lucascalheiros.data.notification

import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.notifications.NewNotificationChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewNotificationChannelImpl @Inject constructor(
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
): NewNotificationChannel {
    override val channel: Channel<Message> = Channel(500, BufferOverflow.DROP_OLDEST)

    fun emit(message: Message) = CoroutineScope(coroutineDispatcher).launch {
        channel.send(message)
    }
}
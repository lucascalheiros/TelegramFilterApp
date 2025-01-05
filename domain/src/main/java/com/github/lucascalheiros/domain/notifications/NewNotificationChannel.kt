package com.github.lucascalheiros.domain.notifications

import com.github.lucascalheiros.domain.model.Message
import kotlinx.coroutines.channels.ReceiveChannel

interface NewNotificationChannel {
    val channel: ReceiveChannel<Message>
}
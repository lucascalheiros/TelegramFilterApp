package com.github.lucascalheiros.data.notification

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message

interface FilteredNotificationEmitter {
    suspend fun onMessage(message: Message, filters: List<Filter>)
}
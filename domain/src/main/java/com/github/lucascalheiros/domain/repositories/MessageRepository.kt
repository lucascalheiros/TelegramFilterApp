package com.github.lucascalheiros.domain.repositories

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message

interface MessageRepository {
    suspend fun searchMessages(filter: Filter): List<Message>
}
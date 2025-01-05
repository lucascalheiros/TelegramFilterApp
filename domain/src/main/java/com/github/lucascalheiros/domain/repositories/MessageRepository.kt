package com.github.lucascalheiros.domain.repositories

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun searchMessages(filter: Filter): List<Message>
    fun onNewMessages(): Flow<Message>
}
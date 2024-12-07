package com.github.lucascalheiros.domain.repositories

import com.github.lucascalheiros.domain.model.ChatInfo
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<ChatInfo>>
    suspend fun update()
}
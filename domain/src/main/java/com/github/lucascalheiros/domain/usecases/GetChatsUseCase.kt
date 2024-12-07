package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(): Flow<List<ChatInfo>> {
        return chatRepository.getChats()
    }

    suspend fun update() {
        chatRepository.update()
    }
}
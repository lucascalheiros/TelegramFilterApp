package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.repositories.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(): Flow<Message> {
        return messageRepository.onNewMessages()
    }
}
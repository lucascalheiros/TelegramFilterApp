package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.repositories.MessageRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
  private val messageRepository: MessageRepository
) {

    suspend operator fun invoke(filter: Filter): List<Message> {
        return messageRepository.searchMessages(filter)
    }
}
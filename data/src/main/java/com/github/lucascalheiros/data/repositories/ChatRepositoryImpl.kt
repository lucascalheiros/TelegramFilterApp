package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.tdlib.TdApi
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val telegramClientWrapper: TelegramClientWrapper
): ChatRepository  {
    override fun getChats(): Flow<List<ChatInfo>> {
        return telegramClientWrapper.chats.map { chatMap ->
            chatMap.values.mapNotNull {
                if (it.title.isBlank()) {
                    return@mapNotNull null
                }
                ChatInfo(it.id, it.title)
            }.sortedBy { it.title }
        }
    }

    override suspend fun update() {
        telegramClientWrapper.send(TdApi.LoadChats(null, 99999))
    }
}
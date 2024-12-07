package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.github.lucascalheiros.data.frameworks.telegram.textContent
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.repositories.MessageRepository
import org.drinkless.tdlib.TdApi.SearchMessages
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val telegramClientWrapper: TelegramClientWrapper
): MessageRepository {

    override suspend fun searchMessages(filter: Filter): List<Message> {
        return filter.queries.map {
            searchMessagesWithQuery(it, filter, "")
        }.flatten()
    }

    private suspend fun searchMessagesWithQuery(query: String, filter: Filter, offset: String = ""): List<Message> {
        val foundMessages = telegramClientWrapper.send(SearchMessages(
            null,
            filter.onlyChannels,
            query,
            offset,
            100,
            null,
            0,
            0
        ))

        val updateAtInSeconds = filter.updateAt / 1000L

        val messages = foundMessages.messages.mapNotNull {
            if (it.date < updateAtInSeconds) {
                return@mapNotNull null
            }
            val chat = telegramClientWrapper.getChat(it.chatId)
            Message(it.id, it.content.textContent(), it.date, chat?.title.orEmpty())
        }

        val nextOffset = foundMessages.nextOffset
        val shouldFetchNextPage = nextOffset.isNotBlank() && messages.lastOrNull()?.date?.let {
            it > updateAtInSeconds
        } == true

        return messages + if (shouldFetchNextPage) searchMessagesWithQuery(query, filter, nextOffset) else listOf()
    }

}
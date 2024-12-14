package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.github.lucascalheiros.data.frameworks.telegram.textContent
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.repositories.MessageRepository
import org.drinkless.tdlib.TdApi
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val telegramClientWrapper: TelegramClientWrapper
) : MessageRepository {

    override suspend fun searchMessages(filter: Filter): List<Message> {
        return searchMessagesWithQuery(filter.queries.joinToString(" "), filter, "")
    }

    private suspend fun searchMessagesWithQuery(
        query: String,
        filter: Filter,
        offset: String = ""
    ): List<Message> {
        val foundMessages = telegramClientWrapper.send(searchMessagesParam(query, filter, offset))
        val messages = foundMessages.getFilteredMessages(filter)
        val nextOffset = foundMessages.nextOffset
        val shouldFetchNextPage = nextOffset.isNotBlank()
        val nextResponse = if (shouldFetchNextPage) searchMessagesWithQuery(
            query,
            filter,
            nextOffset
        ) else listOf()
        return messages + nextResponse
    }

    private fun searchMessagesParam(
        query: String,
        filter: Filter,
        offset: String
    ): TdApi.SearchMessages {
        val dateLimitInSeconds = (filter.dateLimit / 1000L).toInt()
        return TdApi.SearchMessages(
            null,
            filter.onlyChannels,
            query,
            offset,
            100,
            null,
            dateLimitInSeconds,
            0
        )
    }

    private fun TdApi.FoundMessages.getFilteredMessages(filter: Filter): List<Message> {
        return messages.mapNotNull {
            if (!filter.onlyChannels && it.chatId !in filter.chatIds) {
                return@mapNotNull null
            }
            val chat = telegramClientWrapper.getChat(it.chatId)
            Message(it.id, it.content.textContent(), it.date, chat?.title.orEmpty())
        }
    }

}
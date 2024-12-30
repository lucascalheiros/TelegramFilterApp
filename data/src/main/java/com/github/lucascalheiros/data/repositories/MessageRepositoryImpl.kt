package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.github.lucascalheiros.data.frameworks.telegram.textContent
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.repositories.MessageRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.drinkless.tdlib.TdApi
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val telegramClientWrapper: TelegramClientWrapper
) : MessageRepository {

    override suspend fun searchMessages(filter: Filter): List<Message> {
        return when (filter.strategy) {
            FilterStrategy.LocalRegexSearch -> searchMessagesWithRegex(filter)
            FilterStrategy.TelegramQuerySearch -> searchMessagesWithQuery(filter)
        }
    }

    private suspend fun searchMessagesWithRegex(
        filter: Filter
    ): List<Message> = coroutineScope {
        filter.chatIds.map {
            async { getMessagesFromChat(chatId = it, (filter.limitDate / 1000L).toInt()) }
        }.awaitAll().flatten().distinctBy { it.id }.mapNotNull { message ->
            val textContent = message.content.textContent()
            val match = filter.hasMatchInText(textContent)
            if (match) {
                val chat = telegramClientWrapper.getChat(message.chatId)
                Message(message.id, message.content.textContent(), message.date, chat?.title.orEmpty())
            } else null
        }.sortedByDescending { it.date }
    }

    private suspend fun getMessagesFromChat(
        chatId: Long,
        limitDate: Int,
        lastMessageId: Long = 0
    ): List<TdApi.Message> {
        val chatHistory = TdApi.GetChatHistory(chatId, lastMessageId, 0, 100, false)
        val messages = telegramClientWrapper.send(chatHistory).messages
        val lastMessage = messages.lastOrNull()
        val shouldFetchNext = lastMessage != null && lastMessage.date > limitDate
        if (shouldFetchNext) {
            return messages.toList() + getMessagesFromChat(chatId, limitDate, lastMessage!!.id)
        }
        return messages.filter {
            it.date > limitDate
        }
    }

    private suspend fun searchMessagesWithQuery(
        filter: Filter
    ): List<Message> {
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
        val dateLimitInSeconds = (filter.limitDate / 1000L).toInt()
        return TdApi.SearchMessages(
            null,
            false,
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
            if (it.chatId !in filter.chatIds) {
                return@mapNotNull null
            }
            val chat = telegramClientWrapper.getChat(it.chatId)
            Message(it.id, it.content.textContent(), it.date, chat?.title.orEmpty())
        }
    }

}
package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.common.log.logDebug
import com.github.lucascalheiros.data.frameworks.searchtext.SearchTextEngine
import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.github.lucascalheiros.data.frameworks.telegram.textContent
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterType
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.repositories.MessageRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.drinkless.tdlib.TdApi
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val telegramClientWrapper: TelegramClientWrapper,
    private val searchTextEngine: SearchTextEngine
) : MessageRepository {

    override suspend fun searchMessages(filter: Filter): List<Message> {
        return when (filter.filterType) {
            FilterType.TelegramQuerySearch -> searchMessagesWithQuery(filter)
            FilterType.LocalRegexSearch -> searchMessagesWithLocalStrategy(filter)
            FilterType.LocalFuzzySearch -> searchMessagesWithLocalStrategy(filter)
        }
    }

    override fun onNewMessages(): Flow<Message> {
        return telegramClientWrapper.newMessagesChannel.receiveAsFlow()
    }

    private suspend fun getMessagesFromMonitoredChats(filter: Filter): List<TdApi.Message> = coroutineScope{
        logDebug("getMessagesFromMonitoredChats chats: ${filter.chatIds.size}")
        filter.chatIds.filter {
            telegramClientWrapper.getChat(it) != null // include only chats that are verified to exist
        }.map {
            async { getMessagesFromChat(chatId = it, (filter.limitDate / 1000L).toInt()) }
        }.awaitAll().flatten().distinctBy { it.id }
    }

    private suspend fun searchMessagesWithLocalStrategy(
        filter: Filter
    ): List<Message> = coroutineScope {
        getMessagesFromMonitoredChats(filter).mapNotNull { message ->
            val textContent = message.content.textContent()
            val match = searchTextEngine.search(textContent, filter.strategy)
            if (match) {
                val chat = telegramClientWrapper.getChat(message.chatId)
                Message(message.id, textContent, message.date, chat?.title.orEmpty())
            } else null
        }.sortedByDescending { it.date }
    }

    private suspend fun getMessagesFromChat(
        chatId: Long,
        limitDate: Int,
        lastMessageId: Long = 0
    ): List<TdApi.Message> {
        val chatHistory = TdApi.GetChatHistory(chatId, lastMessageId, 0, 100, false)
        logDebug("getMessagesFromChat $chatHistory")
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
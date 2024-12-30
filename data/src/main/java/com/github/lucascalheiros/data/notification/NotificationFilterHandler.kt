package com.github.lucascalheiros.data.notification

import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.data.frameworks.telegram.textContent
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.repositories.FilterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationFilterHandler @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val filterRepository: FilterRepository,
    private val filteredNotificationEmitter: FilteredNotificationEmitter
) {

    fun handleNotification(
        messageTd: TdApi.Message,
        chatTd: TdApi.Chat
    ) {
        CoroutineScope(ioDispatcher).launch(CoroutineExceptionHandler { _, error ->
            logError("Error while handling message: $messageTd", error)

        }) {
            val textContent = messageTd.content.textContent()
            val chatId = chatTd.id
            val filtersMatch = findFilterMatches(textToFilter = textContent, chatId)
            if (filtersMatch.isEmpty()) {
                logError("Non matching filter for message: $messageTd")
            }
            val message = Message(
                messageTd.id,
                textContent,
                messageTd.date,
                chatTd.title
            )
            filteredNotificationEmitter.onMessage(message, filtersMatch)
        }
    }

    private suspend fun findFilterMatches(
        textToFilter: String,
        chatId: Long
    ): List<Filter> {
        return filterRepository.getFilters().filter {
            val isFromMonitoredChat = (chatId in it.chatIds)
            if (!isFromMonitoredChat) {
                return@filter false
            }
            it.hasMatchInText(textToFilter)
        }
    }

}

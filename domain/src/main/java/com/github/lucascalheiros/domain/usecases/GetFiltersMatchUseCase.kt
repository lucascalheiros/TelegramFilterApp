package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.common.di.DefaultDispatcher
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFiltersMatchUseCase @Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val hasMatchInTextUseCase: HasMatchInTextUseCase
) {

    suspend operator fun invoke(message: Message): List<Filter> {
        return findFilterMatches(message.content, message.chatId)
    }

    private suspend fun findFilterMatches(
        textToFilter: String,
        chatId: Long
    ): List<Filter> = withContext(defaultDispatcher) {
        getFilterUseCase.invoke().first().filter {
            val isFromMonitoredChat = (chatId in it.chatIds)
            if (!isFromMonitoredChat) {
                return@filter false
            }
            hasMatchInTextUseCase(textToFilter, it.strategy)
        }
    }
}
package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.reducer

import com.github.lucascalheiros.telegramfilterapp.mocks.MessageMocks
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesUiState
import org.junit.Assert.assertEquals
import org.junit.Test


class FilterMessagesReducerTest {
    private val reducer = FilterMessagesReducer()

    @Test
    fun `test reduce with SetMessages action`() {
        val initialState = FilterMessagesUiState(messages = emptyList(), isLoadingMessages = true)
        val newMessages = listOf(MessageMocks.defaultMessage(), MessageMocks.messageWithDate(0))
        val action = FilterMessagesAction.SetMessages(newMessages)

        val newState = reducer.reduce(initialState, action)

        assertEquals(newMessages, newState.messages)
        assertEquals(false, newState.isLoadingMessages)
    }

    @Test
    fun `test reduce with LoadingMessage action`() {
        val initialState = FilterMessagesUiState(messages = listOf(MessageMocks.defaultMessage()), isLoadingMessages = false)
        val action = FilterMessagesAction.LoadingMessage

        val newState = reducer.reduce(initialState, action)

        assertEquals(true, newState.isLoadingMessages)
    }
}

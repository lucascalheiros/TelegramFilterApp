package com.github.lucascalheiros.telegramfilterapp.mocks

import com.github.lucascalheiros.domain.model.Message

object MessageMocks {
    fun defaultMessage(): Message {
        return Message(
            id = 1L,
            content = "Default Message",
            date = 20240101,
            chatTitle = "User1"
        )
    }

    fun messageFromSender(sender: String): Message {
        return Message(
            id = 2L,
            content = "Message from $sender",
            date = 20240102,
            chatTitle = sender
        )
    }

    fun messageWithContent(content: String): Message {
        return Message(
            id = 3L,
            content = content,
            date = 20240103,
            chatTitle = "User2"
        )
    }

    fun messageWithDate(date: Int): Message {
        return Message(
            id = 4L,
            content = "Message for specific date",
            date = date,
            chatTitle = "User3"
        )
    }
}
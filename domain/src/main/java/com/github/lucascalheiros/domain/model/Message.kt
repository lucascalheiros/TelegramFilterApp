package com.github.lucascalheiros.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Long,
    val content: String,
    val date: Int,
    val chatTitle: String,
    val chatId: Long = 0
)

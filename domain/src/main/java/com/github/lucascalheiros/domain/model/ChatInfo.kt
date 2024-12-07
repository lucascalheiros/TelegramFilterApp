package com.github.lucascalheiros.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatInfo(
    val id: Long,
    val title: String
)
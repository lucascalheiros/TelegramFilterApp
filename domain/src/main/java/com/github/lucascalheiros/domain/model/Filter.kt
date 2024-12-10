package com.github.lucascalheiros.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Filter(
    val id: Long,
    val title: String,
    val queries: List<String>,
    val onlyChannels: Boolean,
    val chatIds: List<Long>,
    val dateLimit: Long
)
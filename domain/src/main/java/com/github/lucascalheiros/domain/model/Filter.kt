package com.github.lucascalheiros.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Filter(
    val id: Long,
    val title: String,
    val queries: List<String>,
    val regex: String,
    val chatIds: List<Long>,
    val limitDate: Long,
    val strategy: FilterStrategy,
    val newMessagesCount: Int = 0
) {
    fun hasMatchInText(text: String): Boolean {
        return when (strategy) {
            FilterStrategy.TelegramQuerySearch -> queries.any { text.contains(it, ignoreCase = true) }
            FilterStrategy.LocalRegexSearch -> Regex(regex).containsMatchIn(text)
        }
    }
}

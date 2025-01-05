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
    val filterType: FilterType,
    val newMessagesCount: Int = 0,
    val fuzzyDistance: Int = 1
) {
    val strategy: FilterStrategy by lazy {
        when (filterType) {
            FilterType.TelegramQuerySearch -> FilterStrategy.TelegramQuery(queries)
            FilterType.LocalRegexSearch -> FilterStrategy.LocalRegex(regex)
            FilterType.LocalFuzzySearch -> FilterStrategy.LocalFuzzy(queries, fuzzyDistance)
        }
    }
}
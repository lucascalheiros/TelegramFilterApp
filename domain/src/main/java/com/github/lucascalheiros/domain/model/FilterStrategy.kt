package com.github.lucascalheiros.domain.model

sealed interface FilterStrategy {
    data class TelegramQuery(val queries: List<String>): FilterStrategy
    data class LocalRegex(val regex: String): FilterStrategy
    data class LocalFuzzy(val queries: List<String>, val distance: Int = 1): FilterStrategy
}
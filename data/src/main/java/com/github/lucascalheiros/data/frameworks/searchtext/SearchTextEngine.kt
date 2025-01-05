package com.github.lucascalheiros.data.frameworks.searchtext

import com.github.lucascalheiros.common.strings.normalizeString
import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.fuzzysearch.FuzzySearch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchTextEngine @Inject constructor() {

    fun search(textToSearch: String, strategy: FilterStrategy): Boolean {
        return when (strategy) {
            is FilterStrategy.TelegramQuery -> {
                val normalizedText = textToSearch.normalizeString()
                textToSearch.isNotBlank() && strategy.queries.all {
                    normalizedText.contains(it, ignoreCase = true)
                }
            }

            is FilterStrategy.LocalRegex -> Regex(strategy.regex).containsMatchIn(textToSearch)

            is FilterStrategy.LocalFuzzy -> {
                val lowerCased = textToSearch.lowercase().normalizeString()
                textToSearch.isNotBlank() && strategy.queries.all {
                    val queryLower = it.lowercase().normalizeString()
                    FuzzySearch().levenshtein(lowerCased, queryLower) <= strategy.distance
                }
            }
        }
    }
}


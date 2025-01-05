package com.github.lucascalheiros.data.mappers

import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.data.model.FilterTypeDb
import com.github.lucascalheiros.data.model.FilterWithQueriesAndChats
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterType

fun Filter.toDb(): FilterDb {
    return FilterDb(
        id = id,
        title = title,
        limitDate = limitDate,
        regex = regex,
        type = filterType.toDb(),
        newMessagesCount = newMessagesCount,
        fuzzyDistance = fuzzyDistance
    )
}

fun FilterWithQueriesAndChats.toModel(): Filter {
    return Filter(
        id = filterDb.id,
        title = filterDb.title,
        queries = queries.map { it.query },
        chatIds = chats.map { it.chatId },
        limitDate = filterDb.limitDate,
        regex = filterDb.regex,
        filterType = filterDb.type.toModel(),
        newMessagesCount = filterDb.newMessagesCount,
        fuzzyDistance = filterDb.fuzzyDistance
    )
}

fun FilterTypeDb.toModel(): FilterType {
    return when (this) {
        FilterTypeDb.TelegramQuerySearch -> FilterType.TelegramQuerySearch
        FilterTypeDb.LocalRegexSearch -> FilterType.LocalRegexSearch
        FilterTypeDb.LocalFuzzySearch -> FilterType.LocalFuzzySearch
    }
}

fun FilterType.toDb(): FilterTypeDb {
    return when (this) {
        FilterType.TelegramQuerySearch -> FilterTypeDb.TelegramQuerySearch
        FilterType.LocalRegexSearch -> FilterTypeDb.LocalRegexSearch
        FilterType.LocalFuzzySearch -> FilterTypeDb.LocalFuzzySearch
    }
}
package com.github.lucascalheiros.data.mappers

import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.data.model.FilterStrategyDb
import com.github.lucascalheiros.data.model.FilterWithQueriesAndChats
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterStrategy

fun Filter.toDb(): FilterDb {
    return FilterDb(
        id = id,
        title = title,
        limitDate = limitDate,
        regex = regex,
        strategy = strategy.toDb(),
        newMessagesCount = newMessagesCount
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
        strategy = filterDb.strategy.toModel(),
        newMessagesCount = filterDb.newMessagesCount
    )
}

fun FilterStrategyDb.toModel(): FilterStrategy {
    return when (this) {
        FilterStrategyDb.TelegramQuerySearch -> FilterStrategy.TelegramQuerySearch
        FilterStrategyDb.LocalRegexSearch -> FilterStrategy.LocalRegexSearch
    }
}

fun FilterStrategy.toDb(): FilterStrategyDb {
    return when (this) {
        FilterStrategy.TelegramQuerySearch -> FilterStrategyDb.TelegramQuerySearch
        FilterStrategy.LocalRegexSearch -> FilterStrategyDb.LocalRegexSearch
    }
}
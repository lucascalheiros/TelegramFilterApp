package com.github.lucascalheiros.data.mappers

import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.data.model.FilterWithQueriesAndChats
import com.github.lucascalheiros.domain.model.Filter

fun Filter.toDb(): FilterDb {
    return FilterDb(
        id = id,
        title = title,
        onlyChannels = onlyChannels,
        updateAt = dateLimit
    )
}

fun FilterWithQueriesAndChats.toModel(): Filter {
    return Filter(
        id = filterDb.id,
        title = filterDb.title,
        queries = queries.map { it.query },
        chatIds = chats.map { it.chatId },
        onlyChannels = filterDb.onlyChannels,
        dateLimit = filterDb.updateAt
    )
}
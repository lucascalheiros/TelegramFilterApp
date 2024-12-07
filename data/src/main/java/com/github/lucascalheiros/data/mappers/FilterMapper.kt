package com.github.lucascalheiros.data.mappers

import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.data.model.FilterToQueriesCrossRefDb
import com.github.lucascalheiros.domain.model.Filter

fun Filter.toDb(): FilterDb {
    return FilterDb(
        id,
        title,
        onlyChannels = onlyChannels,
        updateAt = updateAt
    )
}

fun FilterDb.toModel(
    filterToQueriesCrossRefDb: List<FilterToQueriesCrossRefDb>
): Filter {
    return Filter(
        id,
        title,
        filterToQueriesCrossRefDb.map { it.query },
        onlyChannels = onlyChannels,
        updateAt = updateAt
    )
}
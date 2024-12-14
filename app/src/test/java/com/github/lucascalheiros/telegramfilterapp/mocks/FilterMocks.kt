package com.github.lucascalheiros.telegramfilterapp.mocks

import com.github.lucascalheiros.domain.model.Filter

object FilterMocks {
    fun defaultFilter(): Filter {
        return Filter(
            id = 1L,
            title = "Default Filter",
            queries = listOf("query1", "query2"),
            onlyChannels = false,
            chatIds = listOf(1001L, 1002L, 1003L),
            dateLimit = 1672531200000L // Example timestamp
        )
    }

    fun filterWithEmptyQueries(): Filter {
        return Filter(
            id = 2L,
            title = "Filter with Empty Queries",
            queries = emptyList(),
            onlyChannels = true,
            chatIds = listOf(2001L, 2002L),
            dateLimit = 1672531200000L
        )
    }

    fun filterWithSpecificDateLimit(dateLimit: Long): Filter {
        return Filter(
            id = 3L,
            title = "Filter with Specific Date Limit",
            queries = listOf("specificQuery"),
            onlyChannels = false,
            chatIds = listOf(3001L),
            dateLimit = dateLimit
        )
    }

    fun filterWithNoChatIds(): Filter {
        return Filter(
            id = 4L,
            title = "Filter with No Chat IDs",
            queries = listOf("query3"),
            onlyChannels = true,
            chatIds = emptyList(),
            dateLimit = 1672531200000L
        )
    }
}

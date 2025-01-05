package com.github.lucascalheiros.telegramfilterapp.mocks

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterType

object FilterMocks {
    fun defaultFilter(): Filter {
        return Filter(
            id = 1L,
            title = "Default Filter",
            queries = listOf("query1", "query2"),
            regex = "",
            chatIds = listOf(1001L, 1002L, 1003L),
            limitDate = 1672531200000L,
            filterType = FilterType.LocalRegexSearch
        )
    }

    fun filterWithEmptyQueries(): Filter {
        return Filter(
            id = 2L,
            title = "Filter with Empty Queries",
            queries = emptyList(),
            regex = "",
            chatIds = listOf(2001L, 2002L),
            limitDate = 1672531200000L,
            filterType = FilterType.LocalRegexSearch
        )
    }

    fun filterWithSpecificDateLimit(dateLimit: Long): Filter {
        return Filter(
            id = 3L,
            title = "Filter with Specific Date Limit",
            queries = listOf("specificQuery"),
            regex = "",
            chatIds = listOf(3001L),
            limitDate = dateLimit,
            filterType = FilterType.LocalRegexSearch
        )
    }

    fun filterWithNoChatIds(): Filter {
        return Filter(
            id = 4L,
            title = "Filter with No Chat IDs",
            queries = listOf("query3"),
            regex = "",
            chatIds = emptyList(),
            limitDate = 1672531200000L,
            filterType = FilterType.LocalRegexSearch
        )
    }
}

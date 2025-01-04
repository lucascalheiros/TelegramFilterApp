package com.github.lucascalheiros.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class FilterTest {

    @Test
    fun `hasMatchInText should return true when text contains all query (TelegramQuerySearch)`() {
        val filter = Filter(
            id = 1L,
            title = "Test Filter",
            queries = listOf("hello", "world"),
            regex = "",
            chatIds = listOf(),
            limitDate = 0L,
            strategy = FilterStrategy.TelegramQuerySearch
        )

        assertTrue(filter.hasMatchInText("The hello world is beautiful"))
        assertFalse(filter.hasMatchInText("No matching query here"))
    }

    @Test
    fun `hasMatchInText should return false when text does not contain all query (TelegramQuerySearch)`() {
        val filter = Filter(
            id = 1L,
            title = "Test Filter",
            queries = listOf("hello", "world"),
            regex = "",
            chatIds = listOf(),
            limitDate = 0L,
            strategy = FilterStrategy.TelegramQuerySearch
        )

        assertFalse(filter.hasMatchInText("No queries here"))
        assertFalse(filter.hasMatchInText("This is a hello message"))

    }

    @Test
    fun `hasMatchInText should return true for matching regex (LocalRegexSearch)`() {
        val filter = Filter(
            id = 1L,
            title = "Regex Filter",
            queries = listOf(),
            regex = "\\d{4}-\\d{2}-\\d{2}",
            chatIds = listOf(),
            limitDate = 0L,
            strategy = FilterStrategy.LocalRegexSearch
        )

        assertTrue(filter.hasMatchInText("Date: 2024-12-30"))
        assertFalse(filter.hasMatchInText("No date here"))
    }

    @Test
    fun `hasMatchInText should return false for non-matching regex (LocalRegexSearch)`() {
        val filter = Filter(
            id = 1L,
            title = "Regex Filter",
            queries = listOf(),
            regex = "\\d{4}-\\d{2}-\\d{2}",
            chatIds = listOf(),
            limitDate = 0L,
            strategy = FilterStrategy.LocalRegexSearch
        )

        assertFalse(filter.hasMatchInText("Text without a matching pattern"))
    }
}
package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.reducer

import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.telegramfilterapp.mocks.FilterMocks
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterSettingsReducerTest {
    private val reducer = FilterSettingsReducer()

    @Test
    fun `test reduce with SetFilterStrategy action`() {
        val initialState = FilterSettingsUiState(strategy = FilterStrategy.TelegramQuerySearch)
        val action = FilterSettingsAction.SetFilterStrategy(strategy = FilterStrategy.LocalRegexSearch)

        val newState = reducer.reduce(initialState, action)

        assertEquals(FilterStrategy.LocalRegexSearch, newState.strategy)
    }

    @Test
    fun `test reduce with UpdateTitle action`() {
        val initialState = FilterSettingsUiState(filterTitle = "Old Title")
        val action = FilterSettingsAction.UpdateTitle(title = "New Title")

        val newState = reducer.reduce(initialState, action)

        assertEquals("New Title", newState.filterTitle)
    }

    @Test
    fun `test reduce with SetFilter action`() {
        val initialState = FilterSettingsUiState()
        val filter = FilterMocks.defaultFilter()
        val action = FilterSettingsAction.SetFilter(filter)

        val newState = reducer.reduce(initialState, action)

        assertEquals(filter.title, newState.filterTitle)
        assertEquals(filter.queries, newState.queries)
        assertEquals(filter.strategy, newState.strategy)
        assertEquals(filter.regex, newState.regex)
        assertEquals(filter.chatIds, newState.selectedChatIds)
    }

    @Test
    fun `test reduce with AddQuery action`() {
        val initialState = FilterSettingsUiState(queries = mutableListOf("Query1"))
        val action = FilterSettingsAction.AddQuery(text = "Query2")

        val newState = reducer.reduce(initialState, action)

        assertEquals(listOf("Query1", "Query2"), newState.queries)
    }

    @Test
    fun `test reduce with AddQuery action with trim`() {
        val initialState = FilterSettingsUiState(queries = mutableListOf("Query1"))
        val action = FilterSettingsAction.AddQuery(text = "   Query2    ")

        val newState = reducer.reduce(initialState, action)

        assertEquals(listOf("Query1", "Query2"), newState.queries)
    }

    @Test
    fun `test reduce with RemoveQuery action`() {
        val initialState = FilterSettingsUiState(queries = mutableListOf("Query1", "Query2"))
        val action = FilterSettingsAction.RemoveQuery(index = 1)

        val newState = reducer.reduce(initialState, action)

        assertEquals(listOf("Query1"), newState.queries)
    }

    @Test
    fun `test reduce with Close action`() {
        val initialState = FilterSettingsUiState(close = false)
        val action = FilterSettingsAction.Close

        val newState = reducer.reduce(initialState, action)

        assertEquals(true, newState.close)
    }

    @Test
    fun `test reduce with UpdateRegex action`() {
        val initialState = FilterSettingsUiState(regex = "")
        val action = FilterSettingsAction.UpdateRegex("Test")

        val newState = reducer.reduce(initialState, action)

        assertEquals("Test", newState.regex)
    }
}

package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer

import com.github.lucascalheiros.telegramfilterapp.mocks.FilterMocks
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListUiState
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterState
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.LogoutState
import org.junit.Assert.assertEquals
import org.junit.Test

class FilterListReducerTest {

    private val reducer = FilterListReducer()

    @Test
    fun `test reduce with LogoutAction_Failure`() {
        val initialState = FilterListUiState()
        val action = LogoutAction.Failure

        val newState = reducer.reduce(initialState, action)

        assertEquals(LogoutState.Failure, newState.logoutState)
    }

    @Test
    fun `test reduce with LogoutAction_Handled`() {
        val initialState = FilterListUiState(logoutState = LogoutState.Failure)
        val action = LogoutAction.Handled

        val newState = reducer.reduce(initialState, action)

        assertEquals(LogoutState.Idle, newState.logoutState)
    }

    @Test
    fun `test reduce with LogoutAction_Loading`() {
        val initialState = FilterListUiState()
        val action = LogoutAction.Loading

        val newState = reducer.reduce(initialState, action)

        assertEquals(LogoutState.Loading, newState.logoutState)
    }

    @Test
    fun `test reduce with LogoutAction_Success`() {
        val initialState = FilterListUiState()
        val action = LogoutAction.Success

        val newState = reducer.reduce(initialState, action)

        assertEquals(LogoutState.Success, newState.logoutState)
    }

    @Test
    fun `test reduce with FilterLoadAction_SetLoad(false)`() {
        val initialState = FilterListUiState(filterState = FilterState.Loaded(listOf(FilterMocks.defaultFilter(), FilterMocks.filterWithNoChatIds())))
        val action = FilterLoadAction.SetLoad(false)

        val newState = reducer.reduce(initialState, action)

        assertEquals(FilterState.Loaded(initialState.filters), newState.filterState)
    }

    @Test
    fun `test reduce with FilterLoadAction_SetLoad(true)`() {
        val initialState = FilterListUiState(filterState = FilterState.Loaded(listOf(FilterMocks.defaultFilter(), FilterMocks.filterWithNoChatIds())))
        val action = FilterLoadAction.SetLoad(true)

        val newState = reducer.reduce(initialState, action)

        assertEquals(FilterState.Loading(initialState.filters), newState.filterState)
    }

    @Test
    fun `test reduce with FilterLoadAction_Success`() {
        val initialState = FilterListUiState()
        val newFilters = listOf(FilterMocks.defaultFilter(), FilterMocks.filterWithNoChatIds())
        val action = FilterLoadAction.Success(newFilters)

        val newState = reducer.reduce(initialState, action)

        assertEquals(FilterState.Loaded(newFilters), newState.filterState)
    }
}

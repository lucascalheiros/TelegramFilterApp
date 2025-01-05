package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import com.github.lucascalheiros.MainDispatcherRule
import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.domain.usecases.DeleteFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.LogoutUseCase
import com.github.lucascalheiros.telegramfilterapp.mocks.FilterMocks
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListReducer
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FilterListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var getFilterUseCase: GetFilterUseCase

    @MockK
    lateinit var deleteFilterUseCase: DeleteFilterUseCase

    @MockK
    lateinit var logoutUseCase: LogoutUseCase

    lateinit var viewModel: FilterListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        viewModel = FilterListViewModel(
            getFilterUseCase,
            FilterListReducer(),
            deleteFilterUseCase,
            logoutUseCase,
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on LoadData intent validate Filter states`() = runTest {
        // Keep control to advance execution to prevent loss of conflated states from stateflow
        Dispatchers.setMain(StandardTestDispatcher())

        val states = mutableListOf<FilterListUiState>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.toList(states)
        }

        val firstFilterList = listOf(FilterMocks.defaultFilter(), FilterMocks.filterWithEmptyQueries())

        coEvery { getFilterUseCase.getFilters() } returns firstFilterList

        viewModel.dispatch(FilterListIntent.LoadData)

        advanceUntilIdle()

        assertEquals(3, states.size)

        val initialFilterState = states.removeAt(0).filterState

        assertEquals(FilterState.Idle, initialFilterState)

        val loadingFilterState = states.removeAt(0).filterState

        assertEquals(FilterState.Loading(listOf()), loadingFilterState)

        val successFilterState = states.removeAt(0).filterState

        assertEquals(FilterState.Loaded(firstFilterList), successFilterState)

        val secondFilterList = listOf(FilterMocks.defaultFilter())

        coEvery { getFilterUseCase.getFilters() } returns secondFilterList

        viewModel.dispatch(FilterListIntent.LoadData)

        advanceUntilIdle()

        val secondLoadingFilterState = states.removeAt(0).filterState

        assertEquals(FilterState.Loading(firstFilterList), secondLoadingFilterState)

        val secondSuccessFilterState = states.removeAt(0).filterState

        assertEquals(FilterState.Loaded(secondFilterList), secondSuccessFilterState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on LoadData intent error FailureState will keep filter data`() = runTest {
        val events = mutableListOf<FilterListUiEvent>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.toList(events)
        }

        val firstFilterList = listOf(FilterMocks.defaultFilter(), FilterMocks.filterWithEmptyQueries())

        coEvery { getFilterUseCase.getFilters() } returns firstFilterList

        viewModel.dispatch(FilterListIntent.LoadData)

        coEvery { getFilterUseCase.getFilters() } throws Exception()

        viewModel.dispatch(FilterListIntent.LoadData)

        val filterState = viewModel.state.value.filterState

        assertEquals(FilterState.Loaded(firstFilterList), filterState)

        assertEquals(1, events.size)

        assertEquals(FilterListUiEvent.DataLoadingFailure, events.removeAt(0))
    }
}
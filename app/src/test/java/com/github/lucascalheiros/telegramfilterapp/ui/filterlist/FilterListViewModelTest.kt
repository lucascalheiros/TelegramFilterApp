package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import com.github.lucascalheiros.MainDispatcherRule
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.usecases.DeleteFilterUseCase
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.domain.usecases.LogoutUseCase
import com.github.lucascalheiros.telegramfilterapp.mocks.FilterMocks
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.reducer.FilterListReducer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
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
    fun `on LoadData intent error FailureState will keep filter data`() = runTest(
        UnconfinedTestDispatcher()
    ) {
        val events = mutableListOf<FilterListUiEvent>()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.event.collect {
                events.add(it)
            }
        }

        val firstFilterList =
            listOf(FilterMocks.defaultFilter(), FilterMocks.filterWithEmptyQueries())

        every { getFilterUseCase.invoke() } returns flow {
            emit(firstFilterList)
            throw Exception()
        }

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.collectWithLifecycleScope()
        }

        val filterState = viewModel.state.value.filterState

        assertEquals(FilterState.Loaded(firstFilterList), filterState)

        assertEquals(1, events.size)

        assertEquals(FilterListUiEvent.DataLoadingFailure, events.removeAt(0))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `on LoadData intent validate Filter states`() = runTest {
        val firstFilterList =
            listOf(FilterMocks.defaultFilter(), FilterMocks.filterWithEmptyQueries())

        val filtersStateFlow = MutableStateFlow<List<Filter>?>(null)

        every { getFilterUseCase.invoke() } returns filtersStateFlow.filterNotNull()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.collectWithLifecycleScope()
        }

        assertEquals(FilterState.Idle, viewModel.state.value.filterState)

        filtersStateFlow.value = firstFilterList

        assertEquals(FilterState.Loaded(firstFilterList), viewModel.state.value.filterState)

        val secondFilterList = listOf(FilterMocks.defaultFilter())

        filtersStateFlow.value = secondFilterList

        assertEquals(FilterState.Loaded(secondFilterList), viewModel.state.value.filterState)
    }


}
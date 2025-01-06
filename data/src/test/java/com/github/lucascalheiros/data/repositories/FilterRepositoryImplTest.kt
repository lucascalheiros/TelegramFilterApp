package com.github.lucascalheiros.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.github.lucascalheiros.data.local.AppDatabase
import com.github.lucascalheiros.data.repositories.datasources.FilterLocalDataSource
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class FilterRepositoryImplTest {
    lateinit var db: AppDatabase
    lateinit var filterRepositoryImpl: FilterRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()

        filterRepositoryImpl = FilterRepositoryImpl(FilterLocalDataSource(db.filterDao()))
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun `test empty data returns`() = runTest {
        val filters = filterRepositoryImpl.getFilters().first()
        assertEquals(listOf<Filter>(), filters)
    }

    @Test
    fun `test data insertion`() = runTest {
        val filterData = Filter(0L, "Test", listOf("Query"), ".*", listOf(1L), 0L, FilterType.LocalRegexSearch, 0)

        val id = filterRepositoryImpl.saveFilter(filterData)

        val filterLoaded = filterRepositoryImpl.getFilter(id)

        assertEquals(filterData.copy(id = id), filterLoaded)
    }

    @Test
    fun `test data new message increment`() = runTest {
        val filterData = Filter(0L, "Test", listOf("Query"), ".*", listOf(1L), 0L, FilterType.LocalRegexSearch, 0)

        val id = filterRepositoryImpl.saveFilter(filterData)

        filterRepositoryImpl.incrementNewMessage(id)
        filterRepositoryImpl.incrementNewMessage(id)
        filterRepositoryImpl.incrementNewMessage(id)

        val filterLoaded = filterRepositoryImpl.getFilter(id)

        assertEquals(3, filterLoaded!!.newMessagesCount)
    }

    @Test
    fun `test data new message reset`() = runTest {
        val filterData = Filter(0L, "Test", listOf("Query"), ".*", listOf(1L), 0L, FilterType.LocalRegexSearch, 0)

        val id = filterRepositoryImpl.saveFilter(filterData)

        filterRepositoryImpl.incrementNewMessage(id)
        filterRepositoryImpl.incrementNewMessage(id)
        filterRepositoryImpl.incrementNewMessage(id)

        filterRepositoryImpl.resetNewMessages(id)

        val filterLoaded = filterRepositoryImpl.getFilter(id)

        assertEquals(0, filterLoaded!!.newMessagesCount)
    }

    @Test
    fun `test filter deletion`() = runTest {
        val filterData = Filter(0L, "Test", listOf("Query"), ".*", listOf(1L), 0L, FilterType.LocalRegexSearch, 0)

        val id = filterRepositoryImpl.saveFilter(filterData)

        filterRepositoryImpl.deleteFilter(id)

        val filterLoaded = filterRepositoryImpl.getFilter(id)

        assertNull(filterLoaded)
    }

}
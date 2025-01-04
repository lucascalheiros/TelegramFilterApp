package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.mappers.toDb
import com.github.lucascalheiros.data.notification.FilterDataChangeHandler
import com.github.lucascalheiros.data.repositories.datasources.FilterLocalDataSource
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.repositories.FilterRepository
import javax.inject.Inject


class FilterRepositoryImpl @Inject constructor(
    private val filterLocalDataSource: FilterLocalDataSource,
    private val filterDataChangeHandler: FilterDataChangeHandler
): FilterRepository {

    override suspend fun getFilters(): List<Filter> {
        return filterLocalDataSource.getFilters()
    }

    override suspend fun getFilter(id: Long): Filter? {
        return filterLocalDataSource.getFilter(id)
    }

    override suspend fun saveFilter(filter: Filter): Long {
        val id = filterLocalDataSource.save(
            filterDb = filter.toDb(),
            chatIds = filter.chatIds,
            queries = filter.queries
        )
        filterDataChangeHandler.onFilterDataChanged(getFilters())
        return id
    }

    override suspend fun deleteFilter(id: Long) {
        filterLocalDataSource.deleteFilter(id)
        filterDataChangeHandler.onFilterDataChanged(getFilters())
    }

    override suspend fun incrementNewMessage(id: Long) {
        filterLocalDataSource.incrementNewMessage(id)
    }

    override suspend fun resetNewMessages(id: Long) {
        filterLocalDataSource.resetNewMessages(id)
    }

}
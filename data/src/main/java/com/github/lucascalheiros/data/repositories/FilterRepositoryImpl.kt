package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.mappers.toDb
import com.github.lucascalheiros.data.repositories.datasources.FilterLocalDataSource
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.repositories.FilterRepository
import javax.inject.Inject


class FilterRepositoryImpl @Inject constructor(
    private val filterLocalDataSource: FilterLocalDataSource
): FilterRepository {

    override suspend fun getFilters(): List<Filter> {
        return filterLocalDataSource.getFilters()
    }

    override suspend fun getFilter(id: Long): Filter? {
        return filterLocalDataSource.getFilter(id)
    }

    override suspend fun saveFilter(filter: Filter) {
        filterLocalDataSource.save(
            filterDb = filter.toDb(),
            chatIds = filter.selectedChats.map { it.id },
            queries = filter.queries
        )
    }

}
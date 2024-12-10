package com.github.lucascalheiros.data.repositories.datasources

import com.github.lucascalheiros.data.local.dao.FilterDao
import com.github.lucascalheiros.data.mappers.toModel
import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.domain.model.Filter
import javax.inject.Inject

class FilterLocalDataSource @Inject constructor(
    private val dao: FilterDao
) {

    suspend fun getFilters(): List<Filter> = dao.getFilters().map {
        it.toModel()
    }

    suspend fun getFilter(id: Long): Filter? = dao.getFilter(id)?.toModel()

    suspend fun save(
        filterDb: FilterDb,
        chatIds: List<Long>,
        queries: List<String>
    ) = dao.save(filterDb, chatIds, queries)

    suspend fun deleteFilter(id: Long) {
        dao.deleteFilter(id)
    }
}
package com.github.lucascalheiros.data.repositories.datasources

import com.github.lucascalheiros.data.local.dao.FilterDao
import com.github.lucascalheiros.data.mappers.toModel
import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.domain.model.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterLocalDataSource @Inject constructor(
    private val dao: FilterDao
) {

    fun getFilters(): Flow<List<Filter>> = dao.getFilterWithQueriesAndChats().map { list ->
        list.map { it.toModel() }
    }

    suspend fun getFilter(id: Long): Filter? = dao.getFilterWithQueriesAndChats(id)?.toModel()

    suspend fun save(
        filterDb: FilterDb,
        chatIds: List<Long>,
        queries: List<String>
    ) = dao.save(filterDb, chatIds, queries)

    suspend fun deleteFilter(id: Long) {
        dao.deleteFilter(id)
    }

    suspend fun incrementNewMessage(id: Long) {
        dao.incrementNewMessageCounter(id)
    }

    suspend fun resetNewMessages(id: Long) {
        dao.resetNewMessageCounter(id)
    }

}
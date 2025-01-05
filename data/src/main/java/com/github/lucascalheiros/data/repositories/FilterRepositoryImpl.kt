package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.mappers.toDb
import com.github.lucascalheiros.data.repositories.datasources.FilterLocalDataSource
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.repositories.FilterRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


class FilterRepositoryImpl @Inject constructor(
    private val filterLocalDataSource: FilterLocalDataSource,
): FilterRepository {

    private val filterDataChangeChannel = Channel<Long>(Channel.CONFLATED)

    override suspend fun getFilters(): List<Filter> {
        return filterLocalDataSource.getFilters()
    }

    override suspend fun getFilter(id: Long): Filter? {
        return filterLocalDataSource.getFilter(id)
    }

    override suspend fun saveFilter(filter: Filter): Long {
        return filterLocalDataSource.save(
            filterDb = filter.toDb(),
            chatIds = filter.chatIds,
            queries = filter.queries
        ).also {
            filterDataChangeChannel.send(it)
        }
    }

    override suspend fun deleteFilter(id: Long) {
        filterLocalDataSource.deleteFilter(id)
        filterDataChangeChannel.send(id)
    }

    override suspend fun incrementNewMessage(id: Long) {
        filterLocalDataSource.incrementNewMessage(id)
    }

    override suspend fun resetNewMessages(id: Long) {
        filterLocalDataSource.resetNewMessages(id)
    }

    override fun onFilterListChanged(): Flow<Long> {
        return filterDataChangeChannel.receiveAsFlow()
    }

}
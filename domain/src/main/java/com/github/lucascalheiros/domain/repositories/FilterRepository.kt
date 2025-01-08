package com.github.lucascalheiros.domain.repositories

import com.github.lucascalheiros.domain.model.Filter
import kotlinx.coroutines.flow.Flow

interface FilterRepository {
    fun getFilters(): Flow<List<Filter>>
    suspend fun getFilter(id: Long): Filter?
    suspend fun saveFilter(filter: Filter): Long
    suspend fun deleteFilter(id: Long)
    suspend fun deleteAll()
    suspend fun incrementNewMessage(id: Long)
    suspend fun resetNewMessages(id: Long)
}
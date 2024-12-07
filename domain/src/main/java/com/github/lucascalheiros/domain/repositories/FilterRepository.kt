package com.github.lucascalheiros.domain.repositories

import com.github.lucascalheiros.domain.model.Filter

interface FilterRepository {
    suspend fun getFilters(): List<Filter>
    suspend fun getFilter(id: Long): Filter?
    suspend fun saveFilter(filter: Filter)
    suspend fun deleteFilter(id: Long)
}
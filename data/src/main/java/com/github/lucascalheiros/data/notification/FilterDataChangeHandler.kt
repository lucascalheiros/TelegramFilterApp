package com.github.lucascalheiros.data.notification

import com.github.lucascalheiros.domain.model.Filter

interface FilterDataChangeHandler {
    suspend fun onFilterDataChanged(newState: List<Filter>)
}
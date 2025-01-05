package com.github.lucascalheiros.domain.repositories

import com.github.lucascalheiros.domain.model.FilterStrategy

interface SearchTextRepository {
    fun search(textToSearch: String, strategy: FilterStrategy): Boolean
}
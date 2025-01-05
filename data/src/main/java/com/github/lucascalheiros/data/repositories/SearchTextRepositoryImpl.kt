package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.frameworks.searchtext.SearchTextEngine
import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.domain.repositories.SearchTextRepository
import javax.inject.Inject

class SearchTextRepositoryImpl @Inject constructor(
    private val searchTextEngine: SearchTextEngine
): SearchTextRepository {
    override fun search(textToSearch: String, strategy: FilterStrategy): Boolean {
        return searchTextEngine.search(textToSearch, strategy)
    }
}
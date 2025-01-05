package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.domain.repositories.SearchTextRepository
import javax.inject.Inject

class HasMatchInTextUseCase @Inject constructor(
    private val searchTextRepository: SearchTextRepository
) {

    operator fun invoke(textToSearch: String, strategy: FilterStrategy): Boolean {
        return searchTextRepository.search(textToSearch, strategy)
    }
}
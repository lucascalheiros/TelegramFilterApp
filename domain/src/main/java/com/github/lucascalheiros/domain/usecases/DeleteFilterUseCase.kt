package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.FilterRepository
import javax.inject.Inject

class DeleteFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {

    suspend operator fun invoke(filterId: Long) {
        filterRepository.deleteFilter(filterId)
    }

    suspend operator fun invoke() {
        filterRepository.deleteAll()
    }
}
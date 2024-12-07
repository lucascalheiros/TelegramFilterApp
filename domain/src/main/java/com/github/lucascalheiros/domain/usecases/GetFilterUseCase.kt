package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.repositories.FilterRepository
import javax.inject.Inject

class GetFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {

    suspend fun getFilters(): List<Filter> = filterRepository.getFilters()

    suspend fun getFilter(id: Long): Filter? = filterRepository.getFilter(id)

}
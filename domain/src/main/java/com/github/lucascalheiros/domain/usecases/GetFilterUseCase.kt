package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.repositories.FilterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {

    operator fun invoke(): Flow<List<Filter>> = filterRepository.getFilters()

    suspend operator fun invoke(id: Long): Filter? = filterRepository.getFilter(id)

}
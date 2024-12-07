package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.repositories.FilterRepository
import javax.inject.Inject

class SaveFilterUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {

    suspend operator fun invoke(data: Filter) = filterRepository.saveFilter(data)

}
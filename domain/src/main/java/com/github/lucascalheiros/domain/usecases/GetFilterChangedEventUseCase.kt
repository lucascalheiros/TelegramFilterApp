package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.FilterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterChangedEventUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {

    operator fun invoke(): Flow<Long> = filterRepository.onFilterListChanged()


}
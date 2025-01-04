package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.FilterRepository
import javax.inject.Inject

class IncrementFilterNewMessageUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) {

    suspend operator fun invoke(filterId: Long)  {
        filterRepository.incrementNewMessage(filterId)
    }

}
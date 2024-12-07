package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import javax.inject.Inject

class SendSetupNumberUseCase @Inject constructor(
    private val telegramSetupRepository: TelegramSetupRepository
) {

    suspend operator fun invoke(data: String) = telegramSetupRepository.sendNumber(data)

}
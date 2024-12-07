package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import javax.inject.Inject

class SendSetupPasswordUseCase @Inject constructor(
    private val telegramSetupRepository: TelegramSetupRepository
) {

    suspend operator fun invoke(data: String) = telegramSetupRepository.sendPassword(data)

}
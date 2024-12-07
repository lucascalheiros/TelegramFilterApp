package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import javax.inject.Inject

class SendSetupCodeUseCase @Inject constructor(
    private val telegramSetupRepository: TelegramSetupRepository
) {

    suspend operator fun invoke(data: String) = telegramSetupRepository.sendCode(data)

}
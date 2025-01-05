package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import javax.inject.Inject

class SetupTelegramUseCase @Inject constructor(
    private val telegramSetupRepository: TelegramSetupRepository
) {

    operator fun invoke() {
        telegramSetupRepository.setup()
    }

    fun updatePNToken(token: String) {
        telegramSetupRepository.updatePnToken(token)
    }
}
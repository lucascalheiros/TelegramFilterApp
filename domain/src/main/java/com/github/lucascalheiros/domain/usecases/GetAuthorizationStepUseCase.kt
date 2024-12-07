package com.github.lucascalheiros.domain.usecases

import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import javax.inject.Inject

class GetAuthorizationStepUseCase @Inject constructor(
    private val telegramSetupRepository: TelegramSetupRepository
) {

    operator fun invoke() = telegramSetupRepository.authorizationStep()

}
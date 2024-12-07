package com.github.lucascalheiros.data.repositories

import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.AuthorizationStateReady
import org.drinkless.tdlib.TdApi.AuthorizationStateWaitCode
import org.drinkless.tdlib.TdApi.AuthorizationStateWaitPassword
import org.drinkless.tdlib.TdApi.AuthorizationStateWaitPhoneNumber
import org.drinkless.tdlib.TdApi.UpdateAuthorizationState
import javax.inject.Inject

class TelegramSetupRepositoryImpl @Inject constructor(
    private val telegramClientWrapper: TelegramClientWrapper
): TelegramSetupRepository {
    override fun authorizationStep(): Flow<AuthorizationStep?> {
        return telegramClientWrapper.currentAuthState.map { tdState ->
            if (tdState is UpdateAuthorizationState) {
                when (tdState.authorizationState) {
                    is AuthorizationStateWaitPhoneNumber -> AuthorizationStep.PhoneInput
                    is AuthorizationStateWaitCode -> AuthorizationStep.CodeInput
                    is AuthorizationStateWaitPassword -> AuthorizationStep.PasswordInput
                    is AuthorizationStateReady -> AuthorizationStep.Ready
                    else -> null
                }
            } else null
        }
    }

    override suspend fun sendNumber(data: String) {
        val setPhoneNumber = TdApi.SetAuthenticationPhoneNumber(data, null)
        telegramClientWrapper.send(setPhoneNumber)
    }

    override suspend fun sendCode(data: String) {
        val checkAuthCode = TdApi.CheckAuthenticationCode(data)
        telegramClientWrapper.send(checkAuthCode)
    }

    override suspend fun sendPassword(data: String) {
        val checkAuthenticationPassword = TdApi.CheckAuthenticationPassword(data)
        telegramClientWrapper.send(checkAuthenticationPassword)
    }

}
package com.github.lucascalheiros.domain.repositories

import com.github.lucascalheiros.domain.model.AuthorizationStep
import kotlinx.coroutines.flow.Flow

interface TelegramSetupRepository {
    fun authorizationStep(): Flow<AuthorizationStep?>
    suspend fun sendNumber(data: String)
    suspend fun sendCode(data: String)
    suspend fun sendPassword(data: String)
    suspend fun logout()
}
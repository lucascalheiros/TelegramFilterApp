package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.reducer

import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.TelegramSetupUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class TelegramSetupReducerTest {
    private val reducer = TelegramSetupReducer()

    @Test
    fun `test reduce with UpdateCode action`() {
        val initialState = TelegramSetupUiState(code = "")
        val action = TelegramSetupAction.UpdateCode(code = "123456")

        val newState = reducer.reduce(initialState, action)

        assertEquals("123456", newState.code)
    }

    @Test
    fun `test reduce with UpdatePassword action`() {
        val initialState = TelegramSetupUiState(password = "")
        val action = TelegramSetupAction.UpdatePassword(password = "securePassword")

        val newState = reducer.reduce(initialState, action)

        assertEquals("securePassword", newState.password)
    }

    @Test
    fun `test reduce with UpdatePhoneNumber action`() {
        val initialState = TelegramSetupUiState(phoneNumber = "")
        val action = TelegramSetupAction.UpdatePhoneNumber(phoneNumber = "1234567890")

        val newState = reducer.reduce(initialState, action)

        assertEquals("1234567890", newState.phoneNumber)
    }

    @Test
    fun `test reduce with UpdateStep action`() {
        val initialState = TelegramSetupUiState(step = AuthorizationStep.PhoneInput, isStepLoading = true)
        val action = TelegramSetupAction.UpdateStep(step = AuthorizationStep.CodeInput("000"))

        val newState = reducer.reduce(initialState, action)

        assertEquals(AuthorizationStep.CodeInput("000"), newState.step)
        assertEquals(false, newState.isStepLoading)
    }

    @Test
    fun `test reduce with SetStepLoadingState action`() {
        val initialState = TelegramSetupUiState(isStepLoading = false)
        val action = TelegramSetupAction.SetStepLoadingState(isStepLoading = true)

        val newState = reducer.reduce(initialState, action)

        assertEquals(true, newState.isStepLoading)
    }
}
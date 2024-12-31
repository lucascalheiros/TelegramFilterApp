package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.TelegramSetupIntent
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.TelegramSetupUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelegramSetupScreenContent(
    state: TelegramSetupUiState,
    snackbarHostState: SnackbarHostState,
    send: (TelegramSetupIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar({ Text(stringResource(R.string.telegram_account_setup)) })
        },
        floatingActionButton = {
            if (!state.isStepLoading) {
                FloatingActionButton(
                    onClick = { send(TelegramSetupIntent.NextStep) },
                    modifier = Modifier.alpha(if (state.isActionEnabled) 1f else 0.4f)
                ) {
                    Icon(
                        painterResource(R.drawable.ic_arrow_forward),
                        stringResource(R.string.confirm)
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(state.isStepLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
            AnimatedVisibility(state.isPhoneInputVisible) {
                PhoneNumberInput(
                    number = state.phoneNumber,
                    region = state.displayCountry,
                    onChangeNumber = { send(TelegramSetupIntent.UpdatePhoneNumber(it)) }
                )
            }
            AnimatedVisibility(state.isCodeInputVisible) {
                if (state.step is AuthorizationStep.CodeInput) {
                    CodeInput(
                        value = state.code,
                        step = state.step,
                        onChangeNumber = { send(TelegramSetupIntent.ChangeNumber) },
                        onChange = { send(TelegramSetupIntent.UpdateCode(it)) }
                    )
                }
            }
            AnimatedVisibility(state.isPasswordInputVisible) {
                PasswordInputInput(
                    value = state.password,
                    onCancelSetup = { send(TelegramSetupIntent.CancelSetup) },
                    onChange = { send(TelegramSetupIntent.UpdatePassword(it)) }
                )
            }
        }
    }
}
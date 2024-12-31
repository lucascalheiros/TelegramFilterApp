package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components.TelegramSetupScreenContent
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun TelegramSetupScreen(
    viewModel: TelegramSetupViewModel = hiltViewModel(),
    onSetupFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val snackbarHostState = remember { SnackbarHostState() }
    DisposableEffect(lifecycleOwner) {
        val job = lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.watchStateUpdate()
            }
        }
        onDispose {
            job.cancel()
        }
    }
    val stepReady = state.value.step == AuthorizationStep.Ready
    LaunchedEffect(stepReady) {
        if (stepReady) {
            onSetupFinished()
        }
    }
    HandleUiEvent(viewModel.event, snackbarHostState)
    TelegramSetupScreenContent(state.value, snackbarHostState, viewModel::dispatch)
}

@Composable
private fun HandleUiEvent(
    eventFlow: Flow<TelegramSetupUiEvent>,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        eventFlow.collectLatest {
            launch(CoroutineExceptionHandler { _, throwable ->
                logError("::uiEventHandler", throwable)
            }) {
                when (it) {
                    TelegramSetupUiEvent.SendAuthCodeFailedToast -> snackbarHostState.showSnackbar(
                        context.getString(R.string.wrong_auth_code)
                    )

                    TelegramSetupUiEvent.SendPasswordFailedToast -> snackbarHostState.showSnackbar(
                        context.getString(R.string.wrong_password)
                    )

                    TelegramSetupUiEvent.SendPhoneNumberFailedToast -> snackbarHostState.showSnackbar(
                        context.getString(R.string.error_on_phone_number_send)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PhoneInputPreview() {
    TelegramFilterAppTheme {
        TelegramSetupScreen {}
    }
}
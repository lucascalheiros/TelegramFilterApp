package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components.TelegramSetupScreenContent
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme
import kotlinx.coroutines.launch


@Composable
fun TelegramSetupScreen(
    viewModel: TelegramSetupViewModel = hiltViewModel(),
    onSetupFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
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
    TelegramSetupScreenContent(state.value, viewModel::dispatch)
}


@Preview(showBackground = true)
@Composable
fun PhoneInputPreview() {
    TelegramFilterAppTheme {
        TelegramSetupScreen {}
    }
}
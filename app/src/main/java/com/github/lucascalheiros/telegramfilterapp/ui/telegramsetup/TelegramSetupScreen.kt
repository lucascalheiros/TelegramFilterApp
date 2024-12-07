package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.telegramfilterapp.R
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TelegramSetupScreenContent(
    state: TelegramSetupUiState,
    send: (TelegramSetupIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                {
                    Text(stringResource(R.string.telegram_account_setup))
                }
            )
        },
        floatingActionButton = {
            if (!state.isStepLoading) {
                FloatingActionButton(
                    {
                        send(TelegramSetupIntent.NextStep)
                    },
                ) {
                    Icon(
                        painterResource(R.drawable.ic_arrow_forward),
                        stringResource(R.string.confirm)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isStepLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                return@Column
            }
            when (state.step) {
                AuthorizationStep.PhoneInput -> {
                    PhoneNumberInput(state.phoneNumber) {
                        send(TelegramSetupIntent.UpdatePhoneNumber(it))
                    }
                }

                AuthorizationStep.CodeInput -> {
                    CodeInput(state.code) {
                        send(TelegramSetupIntent.UpdateCode(it))
                    }
                }

                AuthorizationStep.PasswordInput -> {
                    PasswordInputInput(state.password) {
                        send(TelegramSetupIntent.UpdatePassword(it))
                    }
                }

                else -> return@Column
            }
        }
    }
}

@Composable
fun PhoneNumberInput(value: String, onChange: (String) -> Unit) {
    Text(
        text = "Please input your telegram's existing account phone number:",
        textAlign = TextAlign.Center
    )
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        prefix = {
            Text("+")
        },
        label = {
            Text(stringResource(R.string.phone_number))
        }
    )

}

@Composable
fun CodeInput(value: String, onChange: (String) -> Unit) {
    Text(
        text = stringResource(R.string.login_confirmation_code),
        textAlign = TextAlign.Center
    )
    BasicTextField(
        value = value,
        onValueChange = {
            onChange(it.take(5))
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            val shape = RoundedCornerShape(percent = 30)
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(5) { index ->
                    val borderColor = if (value.length == index)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.outline
                    Text(
                        text = value.getOrNull(index)?.toString().orEmpty(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .width(40.dp)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest, shape)
                            .border(BorderStroke(1.dp, borderColor), shape)
                            .padding(vertical = 16.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun PasswordInputInput(value: String, onChange: (String) -> Unit) {
    Text(
        text = stringResource(R.string.account_s_password),
        textAlign = TextAlign.Center
    )
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = {
            Text(stringResource(R.string.password))
        },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                if (passwordVisibility) {
                    Icon(
                        painterResource(R.drawable.ic_visibility_off),
                        stringResource(R.string.hide_password)
                    )
                } else {
                    Icon(
                        painterResource(R.drawable.ic_visibility),
                        stringResource(R.string.show_password)
                    )

                }
            }
        },
    )

}

@Preview(showBackground = true)
@Composable
fun PhoneInputPreview() {
    TelegramFilterAppTheme {
        TelegramSetupScreen {}
    }
}
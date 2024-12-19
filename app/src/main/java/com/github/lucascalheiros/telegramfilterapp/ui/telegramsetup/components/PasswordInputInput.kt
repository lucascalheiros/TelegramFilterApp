package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.github.lucascalheiros.telegramfilterapp.R

@Composable
fun PasswordInputInput(value: String, onChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
}
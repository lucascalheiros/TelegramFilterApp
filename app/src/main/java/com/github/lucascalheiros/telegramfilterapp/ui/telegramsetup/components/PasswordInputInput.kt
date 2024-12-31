package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.R

private const val TAG_CANCEL_SETUP = "CANCEL_SETUP"

@Composable
fun PasswordInputInput(value: String, onCancelSetup: () -> Unit, onChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val textLinkStyles = TextLinkStyles(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.account_s_password))
                append("\n")
                withLink(
                    LinkAnnotation.Clickable(
                        TAG_CANCEL_SETUP,
                        textLinkStyles
                    ) {
                        onCancelSetup()
                    }
                ) {
                    append(stringResource(R.string.click_here_to_cancel_the_setup))
                }
            },
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
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
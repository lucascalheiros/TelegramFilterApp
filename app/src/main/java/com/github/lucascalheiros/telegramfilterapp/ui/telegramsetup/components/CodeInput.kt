package com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.R

@Composable
fun CodeInput(value: String, onChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                .background(
                                    MaterialTheme.colorScheme.surfaceContainerHighest,
                                    shape
                                )
                                .border(BorderStroke(1.dp, borderColor), shape)
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }
        )
    }
}
package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.regex

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.BaseDialog

@Composable
fun RegexInputDialog(
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var queryText by remember { mutableStateOf("") }
    val isValidRegex = remember(queryText) {
        try {
            Regex(queryText)
            queryText.isNotBlank()
        } catch (e: Exception) {
            false
        }
    }
    BaseDialog(
        title = { Text(text = stringResource(R.string.regex_input)) },
        content = {
            TextField(
                value = queryText,
                onValueChange = { queryText = it },
                label = { Text(stringResource(R.string.regex)) },
            )
        },
        actions = {
            TextButton(
                onClick = { onCancel() },
            ) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = { onConfirm(queryText) },
                enabled = isValidRegex
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        onDismissRequest = onCancel
    )
}
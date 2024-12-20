package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.title

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
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.BaseDialog

@Composable
fun TitleInputDialog(
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var queryText by remember { mutableStateOf("") }
    BaseDialog(
        {
            Text(
                text = stringResource(R.string.title_input),
                style = MaterialTheme.typography.titleLarge
            )
        },
        {
            TextField(
                value = queryText,
                onValueChange = { queryText = it },
                label = { Text(stringResource(R.string.title)) },
            )
        },
        {
            TextButton(
                onClick = { onCancel() },
            ) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = { onConfirm(queryText) },
                enabled = queryText.isNotBlank()
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        onCancel
    )
}
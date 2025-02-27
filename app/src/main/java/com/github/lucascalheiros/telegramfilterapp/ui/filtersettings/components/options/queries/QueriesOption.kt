package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.queries

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.SettingItem

@Composable
fun QueriesOption(
    queries: List<String>,
    onRemoveIndex: (Int) -> Unit,
    onAddQuery: (String) -> Unit
) {
    var showTitleInputDialog by remember { mutableStateOf(false) }
    SettingItem(stringResource(R.string.queries), { showTitleInputDialog = true }) {
        Text(
            queries.joinToString(", "),
            modifier = Modifier.fillMaxWidth(0.5f),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
    if (showTitleInputDialog) {
        ManageQueriesSheet(
            queries,
            onRemoveIndex,
            onAddQuery,
            { showTitleInputDialog = false }
        )
    }
}
package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.lucascalheiros.domain.model.ChatInfo
import com.github.lucascalheiros.telegramfilterapp.R

@Composable
fun SelectChatDialog(
    availableChats: List<ChatInfo>,
    selectedChats: List<ChatInfo>,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val selectedChatIds = selectedChats.map { it.id }
    Dialog(
        onDismissRequest = onCancel
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        stringResource(R.string.add_query_text),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(Modifier.height(16.dp))
                HorizontalDivider()
                LazyColumn(Modifier.height(200.dp)) {
                    items(availableChats, { it.id }) {
                        var isChecked by remember { mutableStateOf(it.id in selectedChatIds) }
                        ListItem(
                            headlineContent = {
                                Text(it.title)
                            }, leadingContent = {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = {
                                        isChecked = it
                                    }
                                )
                            }
                        )
                    }
                }
                HorizontalDivider()
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onCancel() },
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(
                        onClick = { }
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}
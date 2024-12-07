package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesIntent
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesUiState
import org.drinkless.tdlib.TdApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun FilterMessagesScreenContent(
    state: FilterMessagesUiState,
    send: (FilterMessagesIntent) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            FilterMessagesTopBar(onBackPressed)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            items(state.messages, { it.id }) {
                Card(Modifier.padding(16.dp)) {
                    Column(Modifier.padding(8.dp)) {
                        Text(it.sender,  style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(it.content)
                        Spacer(Modifier.height(8.dp))
                        Row {
                            val formattedDateTime = LocalDateTime
                                .from(
                                    Instant.ofEpochSecond(it.date.toLong())
                                        .atZone(ZoneId.systemDefault())
                                )
                                .format(
                                    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                                )
                            Spacer(Modifier.weight(1f))
                            Text(formattedDateTime, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.common.datetime.secondsToLocalDateTime
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.linkify
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesUiState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun FilterMessagesScreenContent(
    state: FilterMessagesUiState,
    onBackPressed: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            FilterMessagesTopBar(onBackPressed)
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            AnimatedVisibility(state.isLoadingMessages) {
                CircularProgressIndicator()
            }
            if (state.showNoMessagesInfo) {
                Text(
                    text = stringResource(R.string.there_are_no_message_yet),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.messages, { it.id }) {
                    Card(Modifier.padding(16.dp)) {
                        Column(Modifier.padding(8.dp)) {
                            Text(it.sender, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            SelectionContainer {
                                Text(it.content.linkify())
                            }
                            Spacer(Modifier.height(8.dp))
                            Row {
                                val formattedDateTime = it.date.toLong().secondsToLocalDateTime()
                                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
                                Spacer(Modifier.weight(1f))
                                Text(formattedDateTime, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}


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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.common.datetime.secondsToLocalDateTime
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenu
import com.github.lucascalheiros.telegramfilterapp.ui.components.linkify
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesIntent
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesUiState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterMessagesScreenContent(
    state: FilterMessagesUiState,
    dispatch: (FilterMessagesIntent) -> Unit,
    onBackPressed: () -> Unit = {},
    onNav: (NavRoute) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        state.screenTitle,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    FilterMoreOptionsDropdownMenu(
                        moreIcon = {
                            Icon(
                                painterResource(R.drawable.ic_more_vert),
                                stringResource(R.string.more)
                            )
                        },
                        filterChannelType = state.filter?.let { ChannelType.FilteredMessage(it) },
                        onFilterSettingsClick = {
                            state.filter?.id.let {
                                onNav(NavRoute.FilterSettings(it))
                            }
                        },
                        onDeleteFilterClick = {
                            dispatch(FilterMessagesIntent.DeleteFilter)
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
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
            AnimatedVisibility(state.isLoadingMessages) {
                CircularProgressIndicator()
            }
        }
    }
}


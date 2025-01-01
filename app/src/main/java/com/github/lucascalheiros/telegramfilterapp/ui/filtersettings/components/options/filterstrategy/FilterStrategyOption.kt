package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.options.filterstrategy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.SettingItem
import com.github.lucascalheiros.telegramfilterapp.ui.components.linkify

@Composable
fun FilterStrategyOption(strategy: FilterStrategy, onSelectStrategy: (FilterStrategy) -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }
    SettingItem(stringResource(R.string.strategy), { menuExpanded = true }) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopStart)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                InfoButtonWithDialog(
                    strategy.text(),
                    strategy.infoText()
                )
                Text(strategy.text(), color = MaterialTheme.colorScheme.primary)

            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }) {

                DropdownMenuItem(
                    text = {
                        Text(FilterStrategy.LocalRegexSearch.text())
                    },
                    onClick = {
                        onSelectStrategy(FilterStrategy.LocalRegexSearch)
                        menuExpanded = false
                    },
                    leadingIcon = {
                        InfoButtonWithDialog(
                            FilterStrategy.LocalRegexSearch.text(),
                            FilterStrategy.LocalRegexSearch.infoText()
                        )
                    }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = {
                        Text(FilterStrategy.TelegramQuerySearch.text())
                    },
                    onClick = {
                        onSelectStrategy(FilterStrategy.TelegramQuerySearch)
                        menuExpanded = false
                    },
                    leadingIcon = {
                        InfoButtonWithDialog(
                            FilterStrategy.TelegramQuerySearch.text(),
                            FilterStrategy.TelegramQuerySearch.infoText()
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoButtonWithDialog(
    title: String,
    content: String
) {
    var showInfo by remember { mutableStateOf(false) }
    if (showInfo) {
        BasicAlertDialog({ showInfo = false }) {
            Card {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(painterResource(R.drawable.ic_info), null)
                    Spacer(Modifier.height(16.dp))
                    Text(title, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    Text(content.linkify(), Modifier.fillMaxWidth())
                    Spacer(Modifier.height(16.dp))
                    Row(Modifier.fillMaxWidth()) {
                        Spacer(Modifier.weight(1f))
                        TextButton({ showInfo = false }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                }
            }
        }
    }
    IconButton({
        showInfo = true
    }) {
        Icon(
            painterResource(R.drawable.ic_info),
            stringResource(R.string.info)
        )
    }
}

@Composable
private fun FilterStrategy.text(): String {
    return when (this) {
        FilterStrategy.LocalRegexSearch -> stringResource(R.string.local_regex)
        FilterStrategy.TelegramQuerySearch -> stringResource(R.string.telegram_search)
    }
}

@Composable
private fun FilterStrategy.infoText(): String {
    return when (this) {
        FilterStrategy.LocalRegexSearch -> stringResource(R.string.info_local_regex).trimMargin()
        FilterStrategy.TelegramQuerySearch -> stringResource(R.string.info_telegram_search).trimMargin()
    }
}
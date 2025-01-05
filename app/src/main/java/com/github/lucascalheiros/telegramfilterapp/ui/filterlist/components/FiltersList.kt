package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterType
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType
import com.github.lucascalheiros.telegramfilterapp.ui.components.FilterMoreOptionsDropdownMenu
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme


@Composable
fun FiltersList(
    filters: List<Filter>,
    onOpenFilterMessages: (filterId: Long) -> Unit = {},
    onOpenFilterSettings: (filterId: Long) -> Unit = {},
    onDeleteFilter: (filterId: Long, filterTitle: String) -> Unit = { _, _ -> }
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(filters, { _, info -> info.id }) { index, filter ->
            ListItem(
                modifier = Modifier.clickable {
                    onOpenFilterMessages(filter.id)
                },
                leadingContent = {
                    if (filter.newMessagesCount > 0) {
                        BadgedBox({ Badge { Text(filter.newMessagesCount.toString()) } }) {
                            Icon(painter = painterResource(R.drawable.ic_mail_closed), null)
                        }
                    } else {
                        Icon(painter = painterResource(R.drawable.ic_mail_opened), null)
                    }
                },
                headlineContent = {
                    Text(
                        filter.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text(
                        filter.queryOrRegexFilter(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingContent = {
                    FilterMoreOptionsDropdownMenu(
                        filterChannelType = ChannelType.FilteredMessage(filter),
                        onFilterSettingsClick = {
                            onOpenFilterSettings(filter.id)
                        },
                        onDeleteFilterClick = {
                            onDeleteFilter(filter.id, filter.title)
                        }
                    )
                }
            )
            if (index != filters.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun Filter.queryOrRegexFilter(): String {
    return when (filterType) {
        FilterType.TelegramQuerySearch -> stringResource(
            R.string.query_supporting_text,
            queries.joinToString(", ")
        )

        FilterType.LocalRegexSearch -> stringResource(R.string.regex_supporting_text, regex)
        FilterType.LocalFuzzySearch -> stringResource(
            R.string.fuzzy,
            fuzzyDistance,
            queries.joinToString(", ")
        )
    }
}


@Preview
@Composable
fun FilterListPreview() {
    TelegramFilterAppTheme {
        FiltersList(
            filters = listOf(
                Filter(
                    0,
                    "Filter",
                    listOf("Word 1", "Word 2"),
                    "",
                    listOf(),
                    0,
                    FilterType.TelegramQuerySearch
                ),
                Filter(
                    1,
                    "Filter 2",
                    listOf("Word 1", "Word 2"),
                    ".*",
                    listOf(),
                    0,
                    FilterType.LocalRegexSearch,
                    10
                )
            )
        )
    }
}

package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.FilterStrategy
import com.github.lucascalheiros.telegramfilterapp.R
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
        itemsIndexed(filters, { _, info -> info.id }) { index, info ->
            ListItem(
                modifier = Modifier.clickable {
                    onOpenFilterMessages(info.id)
                },
                headlineContent = {
                    Text(info.title)
                },
                supportingContent = {
                    Text(info.queryOrRegexFilter())
                },
                trailingContent = {
                    Row {
                        IconButton({
                            onOpenFilterSettings(info.id)
                        }) {
                            Icon(
                                painterResource(R.drawable.ic_edit),
                                stringResource(R.string.edit)
                            )
                        }
                        IconButton({
                            onDeleteFilter(info.id, info.title)
                        }) {
                            Icon(
                                painterResource(R.drawable.ic_delete),
                                stringResource(R.string.delete)
                            )
                        }
                    }
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
    return when (strategy) {
        FilterStrategy.TelegramQuerySearch -> stringResource(
            R.string.query_supporting_text,
            queries.joinToString(", ")
        )

        FilterStrategy.LocalRegexSearch -> stringResource(R.string.regex_supporting_text, regex)
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
                    FilterStrategy.TelegramQuerySearch
                )
            )
        )
    }
}
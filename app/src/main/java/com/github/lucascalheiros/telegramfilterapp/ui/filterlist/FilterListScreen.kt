package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute

@Composable
fun FilterListScreen(
    viewModel: FilterListViewModel = hiltViewModel(),
    onNav: (NavRoute) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.dispatch(FilterListIntent.LoadData)
    }
    val state = viewModel.state.collectAsState()
    FilterChatsScreenContent(state.value, onNav)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChatsScreenContent(
    state: FilterListUiState,
    onNav: (NavRoute) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.alert_filter_settings))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton({
                onNav(NavRoute.FilterSettings())
            }) {
                Icon(painterResource(R.drawable.ic_add), stringResource(R.string.add_filter))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier.padding(innerPadding).fillMaxSize()
        ) {
            itemsIndexed(state.filters, { _, info -> info.id }) { index, info ->
                ListItem(
                    modifier = Modifier.clickable {
                        onNav(NavRoute.FilterMessages(info.id))
                    },
                    headlineContent = {
                        Text(info.title)
                    },
                    supportingContent = {
                        Text(stringResource(R.string.query_supporting_text, info.queries.joinToString(", ")))
                    },
                    trailingContent = {
                        IconButton({
                            onNav(NavRoute.FilterSettings(info.id))
                        }) {
                            Icon(painterResource(R.drawable.ic_edit), stringResource(R.string.edit))
                        }
                    }
                )
                if (index != state.filters.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

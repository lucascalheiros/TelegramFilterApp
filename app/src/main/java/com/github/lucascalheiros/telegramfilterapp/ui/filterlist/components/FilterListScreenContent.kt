package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListIntent
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterListScreenContent(
    state: FilterListUiState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    dispatch: (FilterListIntent) -> Unit,
    onNav: (NavRoute) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.filters))
                },
                actions = {
                    if (state.isLogoutOnGoing) {
                        return@TopAppBar
                    }
                    FilterListScreenOptions(
                        onHelp = {},
                        onLogout = { dispatch(FilterListIntent.Logout) })
                }
            )
        },
        floatingActionButton = {
            if (state.isLogoutOnGoing) {
                return@Scaffold
            }
            FloatingActionButton({
                onNav(NavRoute.FilterSettings())
            }) {
                Icon(painterResource(R.drawable.ic_add), stringResource(R.string.add_filter))
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.isLogoutOnGoing) {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .size(60.dp)
                )
                return@Scaffold
            }
            if (state.filters.isEmpty()) {
                Text(
                    stringResource(R.string.you_have_no_filters_to_display_click_on_the_button_or_on_this_text_to_create_a_filter),
                    Modifier
                        .padding(32.dp)
                        .clickable {
                            onNav(NavRoute.FilterSettings())
                        }
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
                return@Scaffold
            }
            FiltersList(
                state.filters,
                onOpenFilterMessages = {
                    onNav(NavRoute.FilterMessages(it))
                },
                onOpenFilterSettings = {
                    onNav(NavRoute.FilterSettings(it))
                },
                onDeleteFilter = { id, title ->
                    dispatch(FilterListIntent.DeleteFilter(id, title))
                }
            )
        }
    }
}

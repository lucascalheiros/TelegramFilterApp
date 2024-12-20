package com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                    Text(stringResource(R.string.alert_filter_settings))
                },
                actions = {
                    if (state.isLogoutOnGoing) {
                        return@TopAppBar
                    }
                    IconButton({ dispatch(FilterListIntent.Logout) }) {
                        Icon(
                            painterResource(R.drawable.ic_logout),
                            stringResource(R.string.logout)
                        )
                    }
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
        Box(Modifier.padding(innerPadding)) {
            if (state.isLogoutOnGoing) {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .size(60.dp)
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
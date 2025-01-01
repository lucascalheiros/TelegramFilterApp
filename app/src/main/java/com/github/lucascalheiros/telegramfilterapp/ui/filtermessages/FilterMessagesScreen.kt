package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.components.FilterMessagesScreenContent

@Composable
fun FilterMessagesScreen(
    viewModel: FilterMessagesViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {},
    onNav: (NavRoute) -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.dispatch(FilterMessagesIntent.LoadData)
    }
    LaunchedEffect(state.close) {
        if (state.close) {
            onBackPressed()
        }
    }
    FilterMessagesScreenContent(state, viewModel::dispatch, onBackPressed, onNav)
}
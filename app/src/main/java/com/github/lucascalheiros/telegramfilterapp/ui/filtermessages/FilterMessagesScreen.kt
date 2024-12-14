package com.github.lucascalheiros.telegramfilterapp.ui.filtermessages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.components.FilterMessagesScreenContent

@Composable
fun FilterMessagesScreen(
    viewModel: FilterMessagesViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.dispatch(FilterMessagesIntent.LoadData)
    }
    val state = viewModel.state.collectAsState()
    FilterMessagesScreenContent(state.value, onBackPressed)
}
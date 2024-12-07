package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.FilterScreenContent

@Composable
fun FilterSettingsScreen(
    viewModel: FilterSettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {}
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.dispatch(FilterSettingsIntent.LoadData)
    }
    LaunchedEffect(state.value.close) {
        if (state.value.close) {
            onBackPressed()
        }
    }
    FilterScreenContent(state.value, viewModel::dispatch, onBackPressed)
}

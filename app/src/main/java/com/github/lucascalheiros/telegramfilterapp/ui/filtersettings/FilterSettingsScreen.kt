package com.github.lucascalheiros.telegramfilterapp.ui.filtersettings

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.ui.components.ComposeAndStartedScope
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.components.FilterScreenContent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun FilterSettingsScreen(
    viewModel: FilterSettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {}
) {
    val state = viewModel.state.collectAsState()
    ComposeAndStartedScope {
        launch {
            viewModel.collectWithLifecycleScope()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.dispatch(FilterSettingsIntent.LoadData)
    }
    LaunchedEffect(state.value.close) {
        if (state.value.close) {
            onBackPressed()
        }
    }
    HandleUiEvent(viewModel.events)
    FilterScreenContent(state.value, viewModel::dispatch, onBackPressed)
}


@Composable
private fun HandleUiEvent(eventFlow: Flow<FilterSettingsUiEvent>) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        eventFlow.collectLatest {
            launch(CoroutineExceptionHandler { _, throwable ->
                logError("::uiEventHandler", throwable)
            }) {
                when (it) {
                    FilterSettingsUiEvent.DataLoadingFailed -> Toast.makeText(
                        context,
                        context.getString(
                            R.string.data_loading_failed
                        ), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
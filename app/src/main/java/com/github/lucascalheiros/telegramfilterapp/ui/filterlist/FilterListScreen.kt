package com.github.lucascalheiros.telegramfilterapp.ui.filterlist

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.common.log.logError
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.components.FilterListScreenContent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun FilterListScreen(
    viewModel: FilterListViewModel = hiltViewModel(),
    onNav: (NavRoute) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.dispatch(FilterListIntent.LoadData)
    }
    HandlePostNotificationPermissionRequest()
    HandleUiEvent(viewModel.event, snackbarHostState)
    HandleLogoutState(state.logoutState, onLogout)
    FilterListScreenContent(state, snackbarHostState, viewModel::dispatch, onNav)
}

@Composable
private fun HandleUiEvent(eventFlow: Flow<FilterListUiEvent>, snackbarHostState: SnackbarHostState) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        eventFlow.collectLatest {
            launch(CoroutineExceptionHandler { _, throwable ->
                logError("::uiEventHandler", throwable)
            }) {
                when (it) {
                    is FilterListUiEvent.DeleteFilter.Failure -> snackbarHostState.showSnackbar(
                        context.getString(
                            R.string.failed_to_delete_filter,
                            it.filterName
                        )
                    )

                    is FilterListUiEvent.DeleteFilter.Success -> snackbarHostState.showSnackbar(
                        context.getString(R.string.filter_deleted_with_success)
                    )

                    FilterListUiEvent.DataLoadingFailure -> snackbarHostState.showSnackbar(
                        context.getString(R.string.data_loading_failed)
                    )
                }
            }
        }
    }
}

@Composable
private fun HandleLogoutState(logoutState: LogoutState, onLogout: () -> Unit) {
    val isLogoutSuccessful = logoutState is LogoutState.Success
    LaunchedEffect(isLogoutSuccessful) {
        if (isLogoutSuccessful) {
            onLogout()
        }
    }
}

@Composable
private fun HandlePostNotificationPermissionRequest() {
    val postNotificationResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->

        }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        askNotificationPermission(context, postNotificationResultLauncher)
    }
}

private fun askNotificationPermission(
    context: Context,
    resultLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            resultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

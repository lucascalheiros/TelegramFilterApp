package com.github.lucascalheiros.telegramfilterapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.domain.usecases.GetAuthorizationStepUseCase
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.notification.NotificationActions
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme
import com.github.lucascalheiros.telegramfilterapp.util.setupSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getAuthorizationStepUseCase: GetAuthorizationStepUseCase

    private var isInitializing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSplashScreen().setKeepOnScreenCondition {
            isInitializing
        }
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            var startDestination by remember { mutableStateOf<NavRoute?>(null) }
            OnStartDestinationLoaded {
                isInitializing = false
                startDestination = it
            }
            HandleNotificationRedirect(
                startDestination,
                navHostController::navigate
            )
            TelegramFilterAppTheme {
                RootScreen(navHostController, startDestination)
            }
        }
    }

    @Composable
    private fun OnStartDestinationLoaded(onStartDestination: (NavRoute) -> Unit) {
        val step by getAuthorizationStepUseCase().collectAsState(null)
        LaunchedEffect(step) {
            if (step == null) {
                return@LaunchedEffect
            }
            if (step == AuthorizationStep.Ready) {
                NavRoute.FilterList
            } else {
                NavRoute.Setup
            }.run(onStartDestination)
        }
    }

    @Composable
    private fun OnNewNotificationActions(onNotificationActions: (NotificationActions) -> Unit) {
        DisposableEffect(Unit) {
            val listener = Consumer<Intent> {
                NotificationActions.OpenFilterMessages.extract(it)?.run(onNotificationActions)
            }
            addOnNewIntentListener(listener)
            onDispose { removeOnNewIntentListener(listener) }
        }
    }

    @Composable
    private fun HandleNotificationRedirect(startDestination: NavRoute?, onRedirect: (NavRoute) -> Unit) {
        var openFilterCurrentAction by remember { mutableStateOf(NotificationActions.OpenFilterMessages.extract(intent)) }
        OnNewNotificationActions {
            if (it is NotificationActions.OpenFilterMessages) {
                openFilterCurrentAction = it
            }
        }
        if (startDestination == null) {
            return
        }
        LaunchedEffect(openFilterCurrentAction, startDestination) {
            val currentAction = openFilterCurrentAction ?: return@LaunchedEffect
            openFilterCurrentAction = null
            if (startDestination == NavRoute.Setup) {
                return@LaunchedEffect
            }
            onRedirect(NavRoute.FilterMessages(currentAction.filterId))
        }
    }
}

package com.github.lucascalheiros.telegramfilterapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.util.Consumer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.domain.usecases.GetAuthorizationStepUseCase
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.notification.NotificationActions
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListScreen
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesScreen
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsScreen
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.TelegramSetupScreen
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme
import com.github.lucascalheiros.telegramfilterapp.util.setupSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getAuthorizationStepUseCase: GetAuthorizationStepUseCase

    private val authStep by lazy {
        getAuthorizationStepUseCase().stateIn(
            MainScope(),
            SharingStarted.Eagerly,
            null
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val openFilterAction = NotificationActions.OpenFilterMessages.extract(intent)
        setupSplashScreen().setKeepOnScreenCondition {
            authStep.value == null
        }
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()
            var openFilterCurrentAction by remember { mutableStateOf(openFilterAction) }
            var startDestination by remember { mutableStateOf<NavRoute?>(null) }
            OnStartDestinationLoaded {
                startDestination = it
            }
            OnNewNotificationActions {
                if (it is NotificationActions.OpenFilterMessages) {
                    openFilterCurrentAction = it
                }
            }
            LaunchedEffect(openFilterCurrentAction, startDestination) {
                val currentAction = openFilterCurrentAction ?: return@LaunchedEffect
                val nonMutableStartDestination = startDestination ?: return@LaunchedEffect
                if (nonMutableStartDestination == NavRoute.Setup) {
                    return@LaunchedEffect
                }
                navHostController.navigate(NavRoute.FilterMessages(currentAction.filterId))
                openFilterCurrentAction = null
            }
            TelegramFilterAppTheme {
                val nonMutableStartDestination = startDestination
                if (nonMutableStartDestination == null) {
                    TemporaryEmptyScreen()
                    return@TelegramFilterAppTheme
                }
                NavHost(
                    navHostController,
                    nonMutableStartDestination
                ) {
                    composable<NavRoute.Setup> {
                        TelegramSetupScreen {
                            navHostController.navigate(NavRoute.FilterList) {
                                popUpTo(NavRoute.Setup) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    composable<NavRoute.FilterList>(
                        enterTransition = {
                            EnterTransition.None
                        },
                        exitTransition = {
                            ExitTransition.None
                        },
                        popEnterTransition = {
                            EnterTransition.None
                        },
                        popExitTransition = {
                            ExitTransition.None
                        }
                    ) {
                        FilterListScreen(
                            onNav = navHostController::navigate,
                            onLogout = {
                                navHostController.navigate(NavRoute.Setup) {
                                    popUpTo<NavRoute.FilterList> {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<NavRoute.FilterSettings>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(400)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(400)
                            )
                        },
                        popEnterTransition = {
                            null
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(400)
                            )
                        }
                    ) {
                        FilterSettingsScreen(
                            onBackPressed = navHostController::navigateUp
                        )
                    }
                    composable<NavRoute.FilterMessages>(
                        enterTransition = {
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start,
                                tween(400)
                            )
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(400)
                            )
                        },
                        popEnterTransition = {
                            null
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End,
                                tween(400)
                            )
                        }
                    ) {
                        FilterMessagesScreen(
                            onBackPressed = navHostController::navigateUp,
                            onNav = navHostController::navigate,
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun OnStartDestinationLoaded(onStartDestination: (NavRoute) -> Unit) {
        val step by authStep.collectAsState()
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
}

@Composable
private fun TemporaryEmptyScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {}
}

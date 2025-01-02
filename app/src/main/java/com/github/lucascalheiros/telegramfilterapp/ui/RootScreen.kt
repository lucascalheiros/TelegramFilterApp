package com.github.lucascalheiros.telegramfilterapp.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListScreen
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesScreen
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsScreen
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.TelegramSetupScreen

@Composable
fun RootScreen(navHostController: NavHostController, startDestination: NavRoute?) {
    if (startDestination == null) {
        TemporaryEmptyScreen()
        return
    }
    NavHost(
        navHostController,
        startDestination
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

@Composable
private fun TemporaryEmptyScreen() {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {}
}

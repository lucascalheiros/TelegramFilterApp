package com.github.lucascalheiros.telegramfilterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.domain.usecases.GetAuthorizationStepUseCase
import com.github.lucascalheiros.telegramfilterapp.navigation.NavRoute
import com.github.lucascalheiros.telegramfilterapp.ui.filterlist.FilterListScreen
import com.github.lucascalheiros.telegramfilterapp.ui.filtermessages.FilterMessagesScreen
import com.github.lucascalheiros.telegramfilterapp.ui.filtersettings.FilterSettingsScreen
import com.github.lucascalheiros.telegramfilterapp.ui.telegramsetup.TelegramSetupScreen
import com.github.lucascalheiros.telegramfilterapp.ui.theme.TelegramFilterAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getAuthorizationStepUseCase: GetAuthorizationStepUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navHostController = rememberNavController()

            TelegramFilterAppTheme {
                NavHost(
                    navHostController,
                    NavRoute.Redirect
                ) {
                    composable<NavRoute.Redirect> {
                        val step = getAuthorizationStepUseCase().collectAsState(null).value
                        LaunchedEffect(step) {
                            if (step == null) {
                                return@LaunchedEffect
                            }
                            val target: Any = if (step == AuthorizationStep.Ready) {
                                NavRoute.FilterList
                            } else {
                                NavRoute.Setup
                            }
                            navHostController.navigate(target) {
                                popUpTo(NavRoute.Redirect) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    composable<NavRoute.Setup> {
                        TelegramSetupScreen {
                            navHostController.navigate(NavRoute.FilterList) {
                                popUpTo(NavRoute.Setup) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                    composable<NavRoute.FilterList> {
                        FilterListScreen(
                            onNav = { navHostController.navigate(it) },
                            onLogout = {
                                navHostController.navigate(NavRoute.Setup) {
                                    popUpTo<NavRoute.FilterList> {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable<NavRoute.FilterSettings> {
                        FilterSettingsScreen {
                            navHostController.navigateUp()
                        }
                    }
                    composable<NavRoute.FilterMessages> {
                        FilterMessagesScreen {
                            navHostController.navigateUp()
                        }
                    }
                }
            }
        }
    }
}

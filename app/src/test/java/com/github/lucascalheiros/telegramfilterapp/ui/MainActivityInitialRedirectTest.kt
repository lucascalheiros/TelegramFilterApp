package com.github.lucascalheiros.telegramfilterapp.ui

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.rule.GrantPermissionRule
import com.github.lucascalheiros.data.di.TelegramSetupRepositoryBinding
import com.github.lucascalheiros.domain.model.AuthorizationStep
import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import com.github.lucascalheiros.telegramfilterapp.util.AndroidLoggingHandler
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@UninstallModules(TelegramSetupRepositoryBinding::class)
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class MainActivityInitialRedirectTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @BindValue @JvmField
    val telegramSetupRepository: TelegramSetupRepository = object : TelegramSetupRepository {
        override fun authorizationStep(): Flow<AuthorizationStep?> {
            return stepFlow
        }

        override suspend fun sendNumber(data: String) {
        }

        override suspend fun sendCode(data: String) {
        }

        override suspend fun sendPassword(data: String) {
        }

        override suspend fun logout() {
        }
    }

    val stepFlow = MutableStateFlow<AuthorizationStep?>(null)

    @Before
    fun init() {
        stepFlow.value = null
        hiltRule.inject()
        AndroidLoggingHandler.setup()
    }

    @Test
    fun `on start up non Ready AuthorizationSet will show setup screen`() = runTest {
        stepFlow.value = AuthorizationStep.PhoneInput
        composeTestRule.onNodeWithText("Telegram account setup").assertIsDisplayed()
    }

    @Test
    fun `on start up Ready AuthorizationSet will show filter list`() = runTest {
        stepFlow.value = AuthorizationStep.Ready
        composeTestRule.onNodeWithText("Filters").assertIsDisplayed()
    }

}


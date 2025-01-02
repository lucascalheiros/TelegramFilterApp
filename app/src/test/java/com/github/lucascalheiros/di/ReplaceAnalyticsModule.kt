package com.github.lucascalheiros.di

import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.telegramfilterapp.di.AnalyticsModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AnalyticsModule::class, AnalyticsModule.Bindings::class]
)
object ReplaceAnalyticsModule {
    @Singleton
    @Provides
    fun analyticsReporter(): AnalyticsReporter = mockk(relaxed = true)
}
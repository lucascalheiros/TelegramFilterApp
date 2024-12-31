package com.github.lucascalheiros.telegramfilterapp.analytics

import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsReporterImpl @Inject constructor(
    private val crashlytics: FirebaseCrashlytics,
    private val analytics: FirebaseAnalytics
): AnalyticsReporter {

    override fun addNonFatalReport(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun logMessage(message: String) {
        crashlytics.log(message)
    }

}
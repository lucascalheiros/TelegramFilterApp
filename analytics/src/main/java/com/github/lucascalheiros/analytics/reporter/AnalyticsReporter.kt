package com.github.lucascalheiros.analytics.reporter

interface AnalyticsReporter {
    fun addNonFatalReport(throwable: Throwable)
    fun logMessage(message: String)
}
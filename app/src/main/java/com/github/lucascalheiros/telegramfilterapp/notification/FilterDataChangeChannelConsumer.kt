package com.github.lucascalheiros.telegramfilterapp.notification

import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.domain.notifications.FilterDataChangeChannel
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelSyncHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterDataChangeChannelConsumer @Inject constructor(
    private val channelSyncHelper: ChannelSyncHelper,
    private val filterDataChangeChannel: FilterDataChangeChannel,
    private val getFilterUseCase: GetFilterUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val analyticsReporter: AnalyticsReporter
) {

    private var lastSync = 0L

    fun consume() = CoroutineScope(dispatcher).launch(CoroutineExceptionHandler { _, throwable ->
        analyticsReporter.addNonFatalReport(throwable)
    }) {
        var job: Job? = null
        for (event in filterDataChangeChannel.channel) {
            // Debounce like behavior
            job?.cancel()
            job = launch {
                delay(delayTime())
                doSync()
            }
        }
    }

    private fun delayTime(minimalDelaySinceLastSync: Long = 2_000): Long {
        val currentTime = System.currentTimeMillis()
        return (minimalDelaySinceLastSync + lastSync - currentTime).coerceAtLeast(0)
    }

    private suspend fun doSync() {
        channelSyncHelper.syncChannels(getFilterUseCase.getFilters())
        lastSync = System.currentTimeMillis()
    }

}
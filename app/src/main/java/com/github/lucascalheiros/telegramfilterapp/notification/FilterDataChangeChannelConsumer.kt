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

    fun consume() = CoroutineScope(dispatcher).launch(CoroutineExceptionHandler { _, throwable ->
        analyticsReporter.addNonFatalReport(throwable)
    }) {
        var job: Job? = null
        for (event in filterDataChangeChannel.channel) {
            // Debounce behavior
            job?.cancel()
            job = launch {
                delay(5_000)
                channelSyncHelper.syncChannels(getFilterUseCase.getFilters())
            }
        }
    }

}
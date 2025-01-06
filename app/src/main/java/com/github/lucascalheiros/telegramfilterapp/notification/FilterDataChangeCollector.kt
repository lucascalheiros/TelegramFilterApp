package com.github.lucascalheiros.telegramfilterapp.notification

import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelSyncHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterDataChangeCollector @Inject constructor(
    private val channelSyncHelper: ChannelSyncHelper,
    private val getFilterUseCase: GetFilterUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val analyticsReporter: AnalyticsReporter
) {

    fun collect() = CoroutineScope(dispatcher).launch(CoroutineExceptionHandler { _, throwable ->
        analyticsReporter.addNonFatalReport(throwable)
    }) {
        getFilterUseCase().collectLatest {
            try {
                channelSyncHelper.syncChannels(it)
            } catch (e: Exception) {
                analyticsReporter.addNonFatalReport(e)
            }
        }
    }

}
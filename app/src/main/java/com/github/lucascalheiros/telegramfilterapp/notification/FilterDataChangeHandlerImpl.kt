package com.github.lucascalheiros.telegramfilterapp.notification

import com.github.lucascalheiros.data.notification.FilterDataChangeHandler
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelSyncHelper
import javax.inject.Inject

class FilterDataChangeHandlerImpl @Inject constructor(
    private val channelSyncHelper: ChannelSyncHelper,
): FilterDataChangeHandler {
    override suspend fun onFilterDataChanged(newState: List<Filter>) {
        channelSyncHelper.syncChannels(newState)
    }
}
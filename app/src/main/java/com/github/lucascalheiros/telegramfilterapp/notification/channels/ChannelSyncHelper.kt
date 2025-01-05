package com.github.lucascalheiros.telegramfilterapp.notification.channels

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import com.github.lucascalheiros.domain.model.Filter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelSyncHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun syncChannels(filters: List<Filter>) {
        val channels = channelsForFilters(filters)
        context.syncFilterChannels(channels)
    }

    fun createChannels(filters: List<Filter>) {
        val channels = channelsForFilters(filters)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        channels.forEach(notificationManager::createChannelFor)
    }

    private fun channelsForFilters(filters: List<Filter>): List<ChannelType> {
        return filters.map { ChannelType.FilteredMessage(it) }
    }

}

private fun NotificationManager.createChannelFor(channel: ChannelType) {
    createNotificationChannel(
        NotificationChannel(channel.channelId, channel.title, IMPORTANCE_DEFAULT)
    )
}

private fun Context.syncFilterChannels(channels: List<ChannelType>) {
    val currentChannelIds = channels.map { it.channelId }
    val notificationManager = getSystemService(NotificationManager::class.java)
    notificationManager.notificationChannels.forEach {
        if (it.id !in currentChannelIds) {
            notificationManager.deleteNotificationChannel(it.id)
        }
    }
    channels.forEach(notificationManager::createChannelFor)
}
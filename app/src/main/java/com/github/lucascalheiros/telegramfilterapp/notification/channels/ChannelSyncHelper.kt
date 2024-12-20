package com.github.lucascalheiros.telegramfilterapp.notification.channels

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelSyncHelper @Inject constructor(
    private val getFilterUseCase: GetFilterUseCase,
    @ApplicationContext private val context: Context
) {

    suspend fun syncChannels() {
        val channels = channelsForFilters()
        context.syncFilterChannels(channels)
    }

    private suspend fun channelsForFilters(): List<ChannelType> {
        return getFilterUseCase.getFilters().map { ChannelType.FilteredMessage(it) }
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
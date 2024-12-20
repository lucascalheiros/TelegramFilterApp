package com.github.lucascalheiros.telegramfilterapp.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import com.github.lucascalheiros.data.notification.FilteredNotificationEmitter
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelSyncHelper
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FilteredNotificationEmitterImpl @Inject constructor(
    private val channelSyncHelper: ChannelSyncHelper,
    @ApplicationContext private val context: Context,
): FilteredNotificationEmitter {
    override suspend fun onMessage(message: Message, filters: List<Filter>) {
        channelSyncHelper.syncChannels()

        val firstFilter = filters.first()

        val notification = Notification.Builder(context, ChannelType.FilteredMessage(firstFilter).channelId)
            .setSmallIcon(R.drawable.ic_filter_list)
            .setContentTitle(firstFilter.title)
            .setContentText(message.content)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(message.id.toInt(), notification)
    }
}
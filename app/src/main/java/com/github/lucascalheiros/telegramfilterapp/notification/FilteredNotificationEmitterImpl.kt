package com.github.lucascalheiros.telegramfilterapp.notification

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.github.lucascalheiros.data.notification.FilteredNotificationEmitter
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.usecases.GetFilterUseCase
import com.github.lucascalheiros.telegramfilterapp.ui.MainActivity
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelSyncHelper
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FilteredNotificationEmitterImpl @Inject constructor(
    private val channelSyncHelper: ChannelSyncHelper,
    private val getFilterUseCase: GetFilterUseCase,
    @ApplicationContext private val context: Context,
) : FilteredNotificationEmitter {
    override suspend fun onMessage(message: Message, filters: List<Filter>) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        channelSyncHelper.syncChannels(getFilterUseCase.getFilters())
        filters.forEach { filter ->

            val action = NotificationActions.OpenFilterMessages(filter.id, message.id)

            val intent = Intent(context, MainActivity::class.java).apply {
                putExtras(action.extras())
                setAction(action.actionIdentifier())
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val notification =
                Notification.Builder(context, ChannelType.FilteredMessage(filter).channelId)
                    .setSmallIcon(R.drawable.app_icon_white)
                    .setContentTitle(filter.title)
                    .setContentText(message.content)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()
            with(NotificationManagerCompat.from(context)) {
                notify(message.id.toInt() + action.hashCode(), notification)
            }
        }
    }
}
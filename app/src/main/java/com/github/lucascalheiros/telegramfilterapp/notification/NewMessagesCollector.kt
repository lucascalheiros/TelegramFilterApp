package com.github.lucascalheiros.telegramfilterapp.notification

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.github.lucascalheiros.analytics.reporter.AnalyticsReporter
import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.domain.model.Message
import com.github.lucascalheiros.domain.usecases.GetFiltersMatchUseCase
import com.github.lucascalheiros.domain.usecases.GetNewMessagesUseCase
import com.github.lucascalheiros.domain.usecases.IncrementFilterNewMessageUseCase
import com.github.lucascalheiros.telegramfilterapp.ui.MainActivity
import com.github.lucascalheiros.telegramfilterapp.R
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelSyncHelper
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewMessagesCollector @Inject constructor(
    private val channelSyncHelper: ChannelSyncHelper,
    @ApplicationContext private val context: Context,
    private val incrementFilterNewMessageUseCase: IncrementFilterNewMessageUseCase,
    private val getFiltersMatchUseCase: GetFiltersMatchUseCase,
    private val getNewMessagesUseCase: GetNewMessagesUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val analyticsReporter: AnalyticsReporter
)  {

    fun collect() = CoroutineScope(dispatcher).launch(CoroutineExceptionHandler { _, throwable ->
        analyticsReporter.addNonFatalReport(throwable)
    }) {
        getNewMessagesUseCase().collect {
            onMessage(it)
        }
    }

    private suspend fun onMessage(message: Message) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val filters = getFiltersMatchUseCase(message)
        // Ensure channels created
        channelSyncHelper.createChannels(filters)
        filters.forEach { filter ->
            incrementFilterNewMessageUseCase(filter.id)

            val action = NotificationActions.OpenFilterMessages(filter.id, message.id)

            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
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
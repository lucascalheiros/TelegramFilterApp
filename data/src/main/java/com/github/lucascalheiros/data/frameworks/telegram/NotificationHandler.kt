package com.github.lucascalheiros.data.frameworks.telegram

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.data.R
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.domain.repositories.FilterRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val filterRepository: FilterRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    fun handleNotification(
        chatId: Long,
        messageId: Long,
        title: String,
        content: String,
        isChannel: Boolean
    ) {
        CoroutineScope(ioDispatcher).launch {
            val filters = try { filterRepository.getFilters() } catch (e: Exception) { return@launch }

            syncFilterChannels(context, filters)

            val firstFilterMatch = filters.firstOrNull { it ->
                val isFromMonitoredChat = (chatId in it.chatIds || isChannel && it.onlyChannels)
                val hasQueryText = it.queries.any { content.contains(it, ignoreCase = true) }
                isFromMonitoredChat && hasQueryText
            } ?: return@launch


            val notification = Notification.Builder(context, firstFilterMatch.channelId())
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content)
                .build()

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.notify(messageId.toInt(), notification)
        }
    }

    private fun Filter.channelId(): String {
        return "Filter$id"
    }

    private fun NotificationManager.createChannelForFilter(filter: Filter) {
        createNotificationChannel(
            NotificationChannel(filter.channelId(), filter.title, IMPORTANCE_DEFAULT)
        )
    }

    private fun syncFilterChannels(context: Context, filters: List<Filter>) {
        val channelIdsFromFilters = filters.map { it.channelId() }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notificationChannels.forEach {
            if (it.id !in channelIdsFromFilters) {
                notificationManager.deleteNotificationChannel(it.id)
            }
        }
        filters.forEach {
            notificationManager.createChannelForFilter(it)
        }
    }
}


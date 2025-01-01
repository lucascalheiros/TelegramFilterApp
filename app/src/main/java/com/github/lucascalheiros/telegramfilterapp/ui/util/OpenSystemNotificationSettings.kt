package com.github.lucascalheiros.telegramfilterapp.ui.util

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.lucascalheiros.domain.model.Filter
import com.github.lucascalheiros.telegramfilterapp.notification.channels.ChannelType

@Composable
fun openNotificationChannelSetting(filter: Filter): () -> Unit {
    return openNotificationChannelSetting(ChannelType.FilteredMessage(filter))
}

@Composable
fun openNotificationChannelSetting(channelType: ChannelType): () -> Unit {
    val context = LocalContext.current
    return {
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            .putExtra(Settings.EXTRA_CHANNEL_ID, channelType.channelId)
        context.startActivity(intent)
    }
}

@Composable
fun openNotificationSettings(): () -> Unit {
    val context = LocalContext.current
    return {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        context.startActivity(intent)
    }
}
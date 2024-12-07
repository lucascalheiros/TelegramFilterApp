package com.github.lucascalheiros.data.frameworks.telegram

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import com.github.lucascalheiros.data.R

fun sendNotification(
    context: Context,
    messageId: Long,
    title: String,
    content: String
) {
    val notificationManager =
        context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(
        NotificationChannel("test", "test", IMPORTANCE_DEFAULT)
    )

    val notification = Notification.Builder(context, "test")
        .setSmallIcon(R.drawable.ic_launcher_round)
        .setContentTitle(title)
        .setContentText(content)
        .build()
    notificationManager.notify(messageId.toInt(), notification)
}
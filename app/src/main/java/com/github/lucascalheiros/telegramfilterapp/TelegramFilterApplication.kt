package com.github.lucascalheiros.telegramfilterapp

import android.app.Application
import com.github.lucascalheiros.domain.usecases.SetupTelegramUseCase
import com.github.lucascalheiros.telegramfilterapp.notification.FilterDataChangeChannelConsumer
import com.github.lucascalheiros.telegramfilterapp.notification.NewNotificationChannelConsumer
import com.github.lucascalheiros.telegramfilterapp.util.AndroidLoggingHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TelegramFilterApplication: Application() {

    @Inject
    lateinit var setupTelegramUseCase: SetupTelegramUseCase

    @Inject
    lateinit var filterDataChangeChannelConsumer: FilterDataChangeChannelConsumer

    @Inject
    lateinit var newNotificationChannelConsumer: NewNotificationChannelConsumer

    override fun onCreate() {
        super.onCreate()
        AndroidLoggingHandler.setup()
        setupTelegramUseCase()
        filterDataChangeChannelConsumer.consume()
        newNotificationChannelConsumer.consume()
    }
}
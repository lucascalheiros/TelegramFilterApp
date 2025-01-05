package com.github.lucascalheiros.telegramfilterapp

import android.app.Application
import com.github.lucascalheiros.domain.usecases.SetupTelegramUseCase
import com.github.lucascalheiros.telegramfilterapp.notification.FilterDataChangeCollector
import com.github.lucascalheiros.telegramfilterapp.notification.NewMessagesCollector
import com.github.lucascalheiros.telegramfilterapp.util.AndroidLoggingHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TelegramFilterApplication: Application() {

    @Inject
    lateinit var setupTelegramUseCase: SetupTelegramUseCase

    @Inject
    lateinit var filterDataChangeCollector: FilterDataChangeCollector

    @Inject
    lateinit var newMessagesCollector: NewMessagesCollector

    override fun onCreate() {
        super.onCreate()
        AndroidLoggingHandler.setup()
        setupTelegramUseCase()
        filterDataChangeCollector.collect()
        newMessagesCollector.collect()
    }
}
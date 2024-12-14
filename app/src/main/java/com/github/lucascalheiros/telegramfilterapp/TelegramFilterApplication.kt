package com.github.lucascalheiros.telegramfilterapp

import android.app.Application
import com.github.lucascalheiros.data.frameworks.telegram.TelegramClientWrapper
import com.github.lucascalheiros.telegramfilterapp.util.AndroidLoggingHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TelegramFilterApplication: Application() {

    @Inject
    lateinit var telegramClientWrapper: TelegramClientWrapper

    override fun onCreate() {
        super.onCreate()
        AndroidLoggingHandler.setup()
        telegramClientWrapper.setup()
    }
}
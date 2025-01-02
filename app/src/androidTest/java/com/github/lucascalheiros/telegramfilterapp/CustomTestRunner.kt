package com.github.lucascalheiros.telegramfilterapp

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return Instrumentation.newApplication(HiltTestApplication::class.java, context)
    }
}
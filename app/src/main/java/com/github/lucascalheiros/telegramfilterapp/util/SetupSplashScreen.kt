package com.github.lucascalheiros.telegramfilterapp.util

import android.animation.AnimatorSet
import android.os.Build
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

fun ComponentActivity.setupSplashScreen(): SplashScreen {
    val installedSplashScreen =  installSplashScreen()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            with(splashScreenView) {
                val scaleX = scaleXTo(2f)
                val scaleY = scaleYTo(2f)
                val alpha = alphaTo(0f)
                AnimatorSet().apply {
                    interpolator = AccelerateDecelerateInterpolator()
                    duration = 500L
                    doOnEnd {
                        splashScreenView.remove()
                    }
                    playTogether(scaleY, scaleX, alpha)
                }.start()
            }
        }
    }
    return installedSplashScreen
}
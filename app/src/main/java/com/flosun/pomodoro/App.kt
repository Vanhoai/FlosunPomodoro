package com.flosun.pomodoro

import android.app.Application
import com.flosunn.core.CoreModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        CoreModule.initialize()
    }
}
package com.flosunn.core

import timber.log.Timber

object CoreModule {

    private const val DEBUG_TAG = "PomodoroDebug"

    fun initialize() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    fun debug(message: String) = Timber.tag(DEBUG_TAG).d(message)

}
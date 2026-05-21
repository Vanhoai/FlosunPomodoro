package com.flosunn.core.libraries.wakeword

import android.app.Application
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WakeWordDetector @Inject constructor(
    private val application: Application,
) {}
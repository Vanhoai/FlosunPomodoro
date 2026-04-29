package com.flosunn.pomodoro.core.functions

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

object ViewFuncs {

    @SuppressLint("ConfigurationScreenWidthHeight")
    @Composable
    fun screenWidthDp(): Int {
        val configuration = LocalConfiguration.current
        return configuration.screenWidthDp
    }

}
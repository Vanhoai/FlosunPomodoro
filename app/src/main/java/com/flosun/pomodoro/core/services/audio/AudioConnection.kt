package com.flosun.pomodoro.core.services.audio

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope

val LocalAudioConnection = staticCompositionLocalOf<AudioConnection?> {
    error("No AudioConnection provided")
}

class AudioConnection(
    context: Context,
    binder: AudioService.AudioBinder,
    scope: CoroutineScope,
) {
    private val service = binder.getService()
    private val exoPlayer = service.exoPlayer

    fun dispose() {}
}
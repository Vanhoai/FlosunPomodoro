package com.flosun.pomodoro.core.services.audio

import androidx.media3.common.MediaItem

data class Queue(
    val items: ArrayDeque<MediaItem> = ArrayDeque(),
    val hasNextAudio: Boolean = false,
)
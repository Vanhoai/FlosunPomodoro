package com.flosun.pomodoro.events

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.common.MediaItem
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class GlobalEvent {

    data class CreateSetting(val accountId: String) : GlobalEvent()
    data class PlayAlarm(
        val mediaItem: MediaItem,
        val onFinished: () -> Unit = {},
    ) : GlobalEvent()

}

@Singleton
class GlobalEventBus @Inject constructor() {

    private val _globalEvent = MutableSharedFlow<GlobalEvent>(
        replay = 0,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val globalEvent = _globalEvent.asSharedFlow()

    fun trySendEvent(event: GlobalEvent) = _globalEvent.tryEmit(event)
    suspend fun sendEvent(event: GlobalEvent) = _globalEvent.emit(event)

}

val LocalEventBus = staticCompositionLocalOf<GlobalEventBus> {
    error("GlobalEventBus not provided")
}
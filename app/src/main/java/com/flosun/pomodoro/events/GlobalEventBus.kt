package com.flosun.pomodoro.events

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class GlobalEvent {

    data class CreateSetting(val accountId: String) : GlobalEvent()

}

@Singleton
class GlobalEventBus @Inject constructor() {

    private val _globalEvent = Channel<GlobalEvent>(Channel.BUFFERED)
    val globalEvent = _globalEvent.receiveAsFlow()

    fun trySendEvent(event: GlobalEvent) = _globalEvent.trySend(event)
    suspend fun sendEvent(event: GlobalEvent) = _globalEvent.send(event)

}

val LocalEventBus = staticCompositionLocalOf<GlobalEventBus> {
    error("GlobalEventBus not provided")
}
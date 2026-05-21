package com.flosun.pomodoro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.flosun.pomodoro.core.services.audio.LocalAudioConnection
import com.flosun.pomodoro.globals.events.GlobalEvent
import com.flosun.pomodoro.globals.events.LocalEventBus
import com.flosun.pomodoro.ui.components.shared.FlashStackedMessages
import com.flosun.pomodoro.ui.components.shared.GlobalAlertMessages
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import com.flosun.pomodoro.ui.components.shared.rememberFlashStackedMessageManager


@Composable
fun AppWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val eventBus = LocalEventBus.current
    val audioConnection = LocalAudioConnection.current ?: return

    val messageManager = rememberFlashStackedMessageManager()
    val messages by messageManager.messages.collectAsState()

    LaunchedEffect(Unit) {
        eventBus.globalEvent.collect { event ->
            when (event) {
                is GlobalEvent.PlayAlarm -> audioConnection.playSoundEvent(
                    mediaItem = event.mediaItem,
                    onFinished = event.onFinished
                )

                else -> {}
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        content()

        FlashStackedMessages(
            messages = messages,
            onDismiss = { messageManager.removeMessage(it) }
        )

        GlobalAlertMessages(modifier = Modifier.fillMaxSize())
        GlobalLoading(modifier = Modifier.fillMaxSize())
    }
}
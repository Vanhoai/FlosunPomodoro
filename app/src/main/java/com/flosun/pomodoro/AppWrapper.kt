package com.flosun.pomodoro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.components.shared.AlertMessageManager
import com.flosun.pomodoro.ui.components.shared.FlashStackedMessages
import com.flosun.pomodoro.ui.components.shared.GlobalAlertMessages
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import com.flosun.pomodoro.ui.components.shared.Message
import com.flosun.pomodoro.ui.components.shared.rememberFlashStackedMessageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Composable
fun AppWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val messageManager = rememberFlashStackedMessageManager()
    val messages by messageManager.messages.collectAsState()

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
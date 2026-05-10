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
import com.flosun.pomodoro.ui.components.shared.FlashStackedMessages
import com.flosun.pomodoro.ui.components.shared.GlobalLoading
import com.flosun.pomodoro.ui.components.shared.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MessageManager @Inject constructor() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun addMessage(title: String, description: String) {
        val newMessage = Message(title = title, description = description)
        _messages.value += newMessage
    }

    fun removeMessage(id: String) {
        _messages.value = _messages.value.filterNot { it.id == id }
    }
}

val LocalMessageManager = compositionLocalOf<MessageManager> { error("No MessageManager provided") }

@Composable
fun rememberMessageManager(): MessageManager {
    return LocalMessageManager.current
}

@Composable
fun AppWrapper(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val messageManager = rememberMessageManager()
    val messages by messageManager.messages.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        content()

        FlashStackedMessages(
            messages = messages,
            onDismiss = { messageManager.removeMessage(it) }
        )

        GlobalLoading(modifier = Modifier.fillMaxSize())
    }
}
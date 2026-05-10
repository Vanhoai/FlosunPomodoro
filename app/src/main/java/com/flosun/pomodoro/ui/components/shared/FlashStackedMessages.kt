package com.flosun.pomodoro.ui.components.shared

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateSetOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosunn.core.extensions.tapGesture
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


data class Message @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val title: String,
    val description: String,
)

// Max skip visible messages to prevent too many cards on the screen
private const val MAX_VISIBLE = 3

@Composable
fun FlashStackedMessages(
    modifier: Modifier = Modifier,
    messages: List<Message> = emptyList(),
    onDismiss: (String) -> Unit = {},
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }
    val visibleMessages = remember(messages, isExpanded) {
        if (isExpanded) messages else messages.takeLast(MAX_VISIBLE)
    }

    val transitionStates = remember { mutableStateMapOf<String, MutableTransitionState<Boolean>>() }

    // Track which messages are currently dismissing to avoid re-adding them during the exit animation
    val dismissingIds = remember { mutableStateSetOf<String>() }
    LaunchedEffect(messages) {
        if (messages.isEmpty()) isExpanded = false
    }

    LaunchedEffect(visibleMessages) {
        val currentIds = visibleMessages.map { it.id }.toSet()
        visibleMessages.forEach { message ->
            if (message.id !in transitionStates) {
                transitionStates[message.id] = MutableTransitionState(false).apply {
                    targetState = true
                }
            }
        }

        transitionStates.keys
            .filter { it !in currentIds }
            .forEach { transitionStates.remove(it) }
    }

    fun dismissMessage(id: String) {
        if (id in dismissingIds) return

        dismissingIds += id
        transitionStates[id]?.targetState = false

        scope.launch {
            delay(400) // Wait for the exit animation to finish before removing the message
            dismissingIds -= id
            transitionStates.remove(id)
            onDismiss(id)
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedVisibility(
            isExpanded,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.2f))
                    .alpha(0.5f)
                    .tapGesture { isExpanded = false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
        ) {
            visibleMessages.forEachIndexed { index, message ->
                val fromTop = if (isExpanded) 0 else visibleMessages.size - 1 - index
                val scaleX = 1f - (fromTop * 0.05f)
                val offsetY = if (isExpanded) {
                    val stepsAbove = visibleMessages.lastIndex - index
                    (stepsAbove * -88).dp
                } else {
                    (fromTop * -12).dp
                }

                val zIndex = index.toFloat()
                val transitionState = transitionStates[message.id] ?: return@forEachIndexed

                key(message.id) {
                    LaunchedEffect(message.id) {
                        delay(5000) // Auto-dismiss after 5 seconds
                        dismissMessage(message.id)
                    }

                    AnimatedVisibility(
                        visibleState = transitionState,
                        enter = slideInVertically {
                            with(density) { 40.dp.roundToPx() }
                        } + fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutHorizontally(
                            animationSpec = tween(500),
                            targetOffsetX = { fullWidth -> fullWidth }
                        ) + fadeOut(),
                        modifier = Modifier.zIndex(zIndex)
                    ) {
                        FlashMessageCard(
                            message = message,
                            scaleX = scaleX,
                            offsetY = offsetY,
                            onClick = if (index == visibleMessages.lastIndex) {
                                { isExpanded = !isExpanded }
                            } else null,
                            onDismiss = { dismissMessage(message.id) }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun FlashMessageCard(
    message: Message,
    scaleX: Float,
    offsetY: Dp,
    onDismiss: () -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    val density = LocalDensity.current
    var dragOffsetX by remember { mutableFloatStateOf(0f) }
    val dismissThreshold = with(density) { 100.dp.toPx() }

    val animatedScaleX by animateFloatAsState(
        targetValue = scaleX,
        animationSpec = tween(500),
        label = "${message.id} scale animation",
    )

    val animatedOffsetY by animateDpAsState(
        targetValue = offsetY,
        animationSpec = tween(500),
        label = "${message.id} offsetY animation",
    )

    MessageCard(
        message = message,
        modifier = Modifier
            .offset(
                x = with(density) { dragOffsetX.toDp() },
                y = animatedOffsetY,
            )
            .scale(scaleX = animatedScaleX, scaleY = 1f)
            .pointerInput(message.id) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (dragOffsetX >= dismissThreshold) {
                            onDismiss()
                        } else {
                            // Snap back to original position
                            dragOffsetX = 0f
                        }
                    },
                    onDragCancel = {
                        dragOffsetX = 0f
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        // Only allow dragging to the right
                        dragOffsetX = (dragOffsetX + dragAmount).coerceAtLeast(0f)
                    }
                )
            }
            .tapGesture { onClick?.invoke() }
    )
}

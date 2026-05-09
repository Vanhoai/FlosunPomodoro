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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import kotlinx.coroutines.delay
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
) {
    val visibleMessages = messages.takeLast(MAX_VISIBLE)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp),
    ) {
        visibleMessages.forEachIndexed { index, message ->
            val fromTop = visibleMessages.size - 1 - index // 0 for top card, 1 for second card, etc
            val scaleX = 1f - (fromTop * 0.05f) // Scale down by 5% for each card below
            val offsetY = (fromTop * 10).dp // Move down by 12.dp for each card below
            val zIndex = index.toFloat() // Higher zIndex for newer cards

            key(message.id) {
                FlashMessageCard(
                    message = message,
                    scaleX = scaleX,
                    offsetY = offsetY,
                    zIndex = zIndex,
                )
            }
        }
    }
}

// Top Card
//AnimatedVisibility(
//visible = isExpanded,
//enter = slideInVertically {
//    // Slide in from 40 dp from the top.
//    with(density) { -40.dp.roundToPx() }
//} + expandVertically(
//// Expand from the top.
//expandFrom = Alignment.Top
//) + fadeIn(
//// Fade in with the initial alpha of 0.3f.
//initialAlpha = 0.3f
//),
//exit = slideOutVertically() + shrinkVertically() + fadeOut()
//)

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun FlashMessageCard(
    message: Message,
    scaleX: Float = 1f,
    offsetY: Dp = 0.dp,
    zIndex: Float = 0f,
) {
    MessageCard(
        message = message,
        modifier = Modifier
            .zIndex(zIndex)
            .offset(y = offsetY)
            .scale(scaleX = scaleX, scaleY = 1f)
    )
}

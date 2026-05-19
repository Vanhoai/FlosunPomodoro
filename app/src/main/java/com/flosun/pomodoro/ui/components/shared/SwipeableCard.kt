package com.flosun.pomodoro.ui.components.shared

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.flosunn.core.extensions.tapGesture
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt

data class SwipeableAction(
    val icon: Int,
    val backgroundColor: Color,
    val onPress: () -> Unit = {},
)

@Composable
fun SwipeableCard(
    modifier: Modifier = Modifier,
    isRevealed: Boolean = false,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    actionWidth: Dp = 58.dp,
    actions: List<SwipeableAction> = emptyList(),
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    var contentHeight by remember { mutableStateOf(0.dp) }

    var containerWidth by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(initialValue = 0f) }
    val scope = rememberCoroutineScope()

    // Two Way Binding for Swipe State
    LaunchedEffect(isRevealed, containerWidth) {
        if (isRevealed) {
            offset.animateTo(-containerWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight)
                .background(
                    color = if (actions.isNotEmpty()) actions.first().backgroundColor else Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Row(
            modifier = Modifier
                .height(contentHeight)
                .onSizeChanged {
                    containerWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            actions.forEachIndexed { index, action ->
                val isFinalAction = index == actions.lastIndex

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(actionWidth)
                        .clip(
                            RoundedCornerShape(
                                topEnd = if (isFinalAction) 8.dp else 0.dp,
                                bottomEnd = if (isFinalAction) 8.dp else 0.dp,
                            )
                        )
                        .background(action.backgroundColor)
                        .tapGesture(Unit) {
                            scope.launch {
                                offset.animateTo(0f)
                                onCollapsed()
                                action.onPress()
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(action.icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    contentHeight = with(density) { it.height.toDp() }
                }
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(-containerWidth, 0f)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value <= -containerWidth / 2f -> {
                                    scope.launch {
                                        offset.animateTo(-containerWidth)
                                        onExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}
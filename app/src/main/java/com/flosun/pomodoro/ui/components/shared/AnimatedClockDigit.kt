package com.flosun.pomodoro.ui.components.shared

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.ui.theme.AppTheme


const val slideDurationMs = 800 // try 120 (snappy) / 800 (luxurious)
const val fadeDurationMs = 450 // try 0 (no fade) / 600 (long crossfade)
const val slideOffsetDivisor = 1 // 1 = full height slide, 2 = half, 4 = subtle

@Composable
fun AnimatedClockDigit(value: Int) {
    Box(
        modifier = Modifier
            .height(300.dp)
            .graphicsLayer { clip = false },
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                val goingUp = targetState > initialState
                if (goingUp) {
                    slideInVertically(
                        animationSpec = tween(slideDurationMs),
                        initialOffsetY = { it / slideOffsetDivisor },
                    ) + fadeIn(animationSpec = tween(fadeDurationMs)) togetherWith
                            slideOutVertically(
                                animationSpec = tween(slideDurationMs),
                                targetOffsetY = { -it / slideOffsetDivisor },
                            ) + fadeOut(animationSpec = tween(fadeDurationMs))
                } else {
                    slideInVertically(
                        animationSpec = tween(slideDurationMs),
                        initialOffsetY = { -it / slideOffsetDivisor },
                    ) + fadeIn(animationSpec = tween(fadeDurationMs)) togetherWith
                            slideOutVertically(
                                animationSpec = tween(slideDurationMs),
                                targetOffsetY = { it / slideOffsetDivisor },
                            ) + fadeOut(animationSpec = tween(fadeDurationMs))
                }
            },
            label = "digit_$value",
            modifier = Modifier
                .wrapContentSize(unbounded = true)
                .graphicsLayer { clip = false },
        ) { animatedValue ->
            Text(
                text = "$animatedValue",
                fontSize = 220.sp,
                style = AppTheme.typography.clock,
                color = Color.White,
            )
        }
    }
}
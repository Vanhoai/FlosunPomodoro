package com.flosun.pomodoro.presentation.graph

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.onLongPress
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.cos
import kotlin.math.sin

private const val SPRING_STIFFNESS = 500f
private const val SPRING_DAMPING = 0.55f
private const val ARC_DEG = 120f
private const val RADIUS_DP = 80f
private const val STAGGER_MS = 50L

data class QuickAction(
    val icon: Int,
    val label: String,
    val color: Color,
)

@Composable
fun BottomTabs(
    currentBottomBarScreen: BottomNavRoute,
    onClick: (BottomNavRoute) -> Unit,
    onOpenAIAssistant: (Offset) -> Unit,
) {
    var position by remember { mutableStateOf(Offset.Zero) }

    val quickActions = listOf(
        QuickAction(R.drawable.ic_star, "Focus Mode", Color(0xFF6ED1CF)),
        QuickAction(R.drawable.ic_grid, "AI Assistant", Color(0xFF7274ED)),
        QuickAction(R.drawable.ic_hourglass, "History", Color(0xFFFFB356)),
    )

    val fabCount = quickActions.size

    val progresses = remember(quickActions.size) { List(quickActions.size) { Animatable(0f) } }
    var isExpanded by remember { mutableStateOf(false) }
    val fabRotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = spring(stiffness = SPRING_STIFFNESS, dampingRatio = SPRING_DAMPING),
        label = "FAB Rotation"
    )

    LaunchedEffect(isExpanded) {
        progresses.forEachIndexed { i, p ->
            launch {
                val staggerIdx = if (isExpanded) i else fabCount - 1 - i
                delay(staggerIdx * STAGGER_MS)
                p.animateTo(
                    targetValue = if (isExpanded) 1f else 0f,
                    animationSpec = spring(
                        stiffness = SPRING_STIFFNESS,
                        dampingRatio = SPRING_DAMPING,
                    ),
                )
            }
        }
    }

    val density = LocalDensity.current
    val radiusPx = with(density) { RADIUS_DP.dp.toPx() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        bottomNavRoutes.forEach { destination ->
            if (destination == BottomNavRoute.More) {
                return@forEach Box(
                    modifier = Modifier
                        .graphicsLayer {
                            translationY = -40.dp.toPx()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    progresses.forEachIndexed { i, animatable ->
                        val sweepStart = 180f + (180f - ARC_DEG) / 2f
                        val angleDeg = sweepStart + (i / (fabCount - 1f)) * ARC_DEG

                        val rad = Math.toRadians(angleDeg.toDouble())
                        val targetX = (radiusPx * cos(rad)).toFloat()
                        val targetY = (radiusPx * sin(rad)).toFloat()

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .graphicsLayer {
                                    val v = animatable.value
                                    translationX = targetX * v
                                    translationY = targetY * v
                                    val s = 0.4f + 0.6f * v
                                    scaleX = s
                                    scaleY = s
                                    alpha = v
                                }
                                .clip(CircleShape)
                                .background(quickActions[i].color)
                                .rippleEffectClickable {},
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(id = quickActions[i].icon),
                                contentDescription = "FAB Item $i",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .graphicsLayer { rotationZ = fabRotation }
                            .onGloballyPositioned {
                                val rect = it.boundsInRoot()
                                position = rect.center
                            }
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(AppTheme.colors.primaryColor)
                            .dropShadow(
                                shape = CircleShape,
                                shadow = Shadow(
                                    color = AppTheme.colors.primaryColor,
                                    alpha = 0.6f,
                                    offset = DpOffset(0.dp, 4.dp),
                                    radius = 16.dp,
                                    spread = 20.dp,
                                ),
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    // onPress = { isExpanded = !isExpanded },
                                    onLongPress = {
                                        // Move offset y to bottom
                                        val readOffset = position
                                        onOpenAIAssistant(readOffset)
                                    },
                                )
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = destination.icon),
                            contentDescription = destination.title,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .tapGesture { onClick(destination) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = destination.title,
                        tint = if (currentBottomBarScreen == destination) AppTheme.colors.primaryColor else Color(
                            0xFF8C8C8C
                        ),
                        modifier = Modifier.size(26.dp)
                    )

                    Text(
                        text = destination.title,
                        color = if (currentBottomBarScreen == destination) AppTheme.colors.primaryColor else Color(
                            0xFF8C8C8C
                        ),
                        fontSize = 14.sp,
                    )
                }
            }
        }
    }
}
package com.flosunn.pomodoro.presentation.swipe.tasks

import android.annotation.SuppressLint
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.shared.SharedSwipeHeading
import com.flosunn.pomodoro.ui.components.shared.TaskCard
import com.flosunn.pomodoro.ui.theme.AppTheme
import timber.log.Timber


@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TasksView(navBackStack: NavBackStack<NavKey>) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.toFloat()
    val screenHeight = configuration.screenHeightDp.toFloat()
    val density = LocalDensity.current

    var isDialogOpen by remember { mutableStateOf(false) }

    val scaleAnimation by animateFloatAsState(
        targetValue = if (isDialogOpen) 0.95f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "ScaleAnimation"
    )

    val animationWidth by animateDpAsState(
        targetValue = if (isDialogOpen) (screenWidth - 20 * 2).dp else 52.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "WidthAnimation"
    )

    val animationHeight by animateDpAsState(
        targetValue = if (isDialogOpen) 300.dp else 52.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "HeightAnimation"
    )

    val borderAnimation by animateDpAsState(
        targetValue = if (isDialogOpen) AppTheme.sizing.borderMedium else 26.dp,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "BorderAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                val bounds = it.boundsInRoot()
                Timber.tag("TasksView").d("Bounds: $bounds")
            },
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SharedSwipeHeading(onPressAvatar = { navBackStack.add(NavRoute.Account) })
            OptionsSlider()
            Text(
                text = "8 Tasks",
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            )

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(10) {
                    TaskCard(modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .width(animationWidth)
                .height(animationHeight)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(borderAnimation)
                )
        )

        Box(
            modifier = Modifier
                .padding(20.dp)
                .size(52.dp)
                .clip(CircleShape)
                .background(AppTheme.colors.primaryColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = { if (!isDialogOpen) isDialogOpen = true },
                    enabled = !isDialogOpen
                )
                .onGloballyPositioned {
                    val bounds = it.boundsInRoot()
                    Timber.tag("FAB").d("Bounds: ${bounds.center}")
                },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_grid),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White,
            )
        }
    }
}

val options = listOf(
    "Active",
    "Completed",
    "Failed",
)

val SLIDE_SPACING = 2.dp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun OptionsSlider() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val containerWidth = screenWidth - 40.dp
    val optionWidth = (containerWidth - SLIDE_SPACING * (options.size + 1)) / options.size

    var selectedOption by remember { mutableIntStateOf(0) }
    val offset = animateDpAsState(
        targetValue = (optionWidth + SLIDE_SPACING) * selectedOption + SLIDE_SPACING,
        animationSpec = spring(
            stiffness = Spring.StiffnessMediumLow,
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),
        label = "SliderOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .height(40.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(optionWidth)
                .height(36.dp)
                .offset(x = offset.value)
                .background(
                    color = AppTheme.colors.primaryColor,
                    shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                ),
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .width(optionWidth)
                        .height(40.dp)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                selectedOption = index
                            }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = option,
                        color = if (selectedOption == index) Color.White else Color(
                            0xFF636363
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
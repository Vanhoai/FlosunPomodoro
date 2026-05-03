package com.flosun.pomodoro.presentation.swipe.home.fullscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlinx.coroutines.delay
import timber.log.Timber
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ContextCastToActivity")
@Composable
fun FullScreenView(navBackStack: NavBackStack<NavKey>) {
    val configuration = LocalConfiguration.current

    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentTime = LocalTime.now()
        }
    }

    val h1 = (currentTime.hour / 10).toString()
    val h2 = (currentTime.hour % 10).toString()
    val m1 = (currentTime.second / 10).toString()
    val m2 = (currentTime.second % 10).toString()

    var isLandscape by remember { mutableStateOf(false) }
    val fontSize by remember {
        derivedStateOf {
            if (isLandscape) 220 else 140
        }
    }

    val dotSize by remember {
        derivedStateOf {
            if (isLandscape) 40.dp else 20.dp
        }
    }

    LaunchedEffect(Unit) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                // Landscape orientation
                isLandscape = true
                Timber.tag("FullScreenView").d("Landscape orientation")
            }

            else -> {
                // Portrait orientation
                isLandscape = false
                Timber.tag("FullScreenView").d("Portrait orientation")
            }
        }
    }

    Scaffold(containerColor = Color(0xFF1C1C1C)) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedClockDigit(
                    value = h1,
                    fontSize = fontSize,
                    gradientColors = listOf(Color(0xFFFF4848), Color(0xFFFF8C00)),
                    rotation = -4f,
                    offsetX = 20.dp
                )

                AnimatedClockDigit(
                    value = h2,
                    fontSize = fontSize,
                    gradientColors = listOf(Color(0xFFC7FF2D), Color(0xFFFF9617)),
                    rotation = 4f,
                    offsetX = 10.dp
                )

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(Color.White),
                    )

                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .clip(CircleShape)
                            .background(Color.White),
                    )
                }

                AnimatedClockDigit(
                    value = m1,
                    fontSize = fontSize,
                    gradientColors = listOf(Color(0xFFFFFF39), Color(0xFF89FF83)),
                    rotation = -4f,
                    offsetX = (-10).dp
                )

                AnimatedClockDigit(
                    value = m2,
                    fontSize = fontSize,
                    gradientColors = listOf(Color(0xFF89FF83), Color(0xFF2BCAFF)),
                    rotation = 4f,
                    offsetX = (-20).dp
                )
            }
        }
    }
}

@Composable
fun AnimatedClockDigit(
    value: String,
    fontSize: Int,
    gradientColors: List<Color>,
    rotation: Float,
    offsetX: Dp
) {
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            slideInVertically(
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
                initialOffsetY = { fullHeight -> -fullHeight }
            ) + fadeIn(
                animationSpec = tween(300)
            ) togetherWith slideOutVertically(
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
                targetOffsetY = { fullHeight -> fullHeight }
            ) + fadeOut(
                animationSpec = tween(300)
            )
        },
        label = "digit_$value",
        modifier = Modifier
            .rotate(rotation)
            .offset(x = offsetX)
            .wrapContentSize(unbounded = true),
    ) { animatedValue ->
        Text(
            text = animatedValue,
            fontSize = fontSize.sp,
            style = AppTheme.typography.clock.copy(
                brush = Brush.verticalGradient(colors = gradientColors)
            ),
        )
    }
}
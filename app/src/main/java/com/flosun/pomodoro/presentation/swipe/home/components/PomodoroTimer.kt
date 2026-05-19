package com.flosun.pomodoro.presentation.swipe.home.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.TimerModeKey
import com.flosun.pomodoro.core.functions.ViewFuncs
import com.flosun.pomodoro.core.services.pomodoro.SessionType
import com.flosun.pomodoro.core.services.pomodoro.TimerMode
import com.flosun.pomodoro.core.services.pomodoro.TimerState
import com.flosun.pomodoro.rememberPomodoroService
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.libraries.datastore.rememberEnumPreference

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PomodoroTimer(
    strokeWidth: Dp = 22.dp,
    modifier: Modifier,
) {
    val pomodoroService = rememberPomodoroService()
    var timerMode by rememberEnumPreference(TimerModeKey, TimerMode.COUNTDOWN)

    val session by pomodoroService.session.collectAsState()

    val totalSeconds = session.totalTime
    val remainTime = session.remainTime
    val elapsedTime = session.elapsedTime

    val isRunning = session.state == TimerState.RUNNING
    val sessionName = when (session.type) {
        SessionType.WORK -> "Focus Time"
        SessionType.SHORT_BREAK -> "Short Break"
        SessionType.LONG_BREAK -> "Long Break"
    }

    val screenWidthDp = ViewFuncs.screenWidthDp()
    val arcSize = (screenWidthDp - 20 * 2 - 40 * 2).dp

    val progress = when (timerMode) {
        TimerMode.COUNTDOWN -> if (totalSeconds > 0) {
            remainTime.toFloat() / totalSeconds.toFloat()
        } else {
            1f
        }

        TimerMode.INFINITY -> 1f
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing),
        label = "Arc Progress"
    )

    val timeText = when (timerMode) {
        TimerMode.COUNTDOWN -> {
            val minutes = remainTime / 60
            val seconds = remainTime % 60
            "%02d:%02d".format(minutes, seconds)
        }

        TimerMode.INFINITY -> {
            val minutes = elapsedTime / 60
            val seconds = elapsedTime % 60
            "%02d:%02d".format(minutes, seconds)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
            )
            .padding(horizontal = 40.dp)
            .padding(top = 40.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(arcSize),
            contentAlignment = Alignment.Center,
        ) {
            ArcProgress(
                progress = animatedProgress,
                arcSize = arcSize,
                strokeWidth = strokeWidth,
                foreground = AppTheme.colors.primaryColor,
                track = Color(0xFFE1E1E1),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = timeText,
                    fontSize = 48.sp,
                    style = AppTheme.typography.clock,
                    modifier = Modifier.padding()
                )

                Text(
                    text = sessionName,
                    fontSize = 20.sp,
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-20).dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.primaryColor.copy(alpha = 0.1f))
                    .rippleEffectClickable { pomodoroService.reset() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = null,
                    tint = AppTheme.colors.primaryColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.primaryColor)
                    .rippleEffectClickable {
                        if (isRunning) pomodoroService.pause()
                        else pomodoroService.play()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(if (isRunning) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(start = if (isRunning) 0.dp else 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(AppTheme.colors.primaryColor.copy(alpha = 0.1f))
                    .rippleEffectClickable { pomodoroService.skip() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_next),
                    contentDescription = null,
                    tint = AppTheme.colors.primaryColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ArcProgress(
    progress: Float,
    arcSize: Dp,
    strokeWidth: Dp,
    foreground: Color,
    track: Color,
) {
    Canvas(
        modifier = Modifier.size(arcSize),
    ) {
        val sw = strokeWidth.toPx()
        val padding = sw / 2f
        val diameter = size.minDimension - sw

        val totalSweep = 240f
        val startAngle = 150f

        val topLeft = Offset(padding, padding)
        val arcSz = Size(diameter, diameter)

        drawArc(
            color = track,
            startAngle = startAngle,
            sweepAngle = totalSweep,
            useCenter = false,
            topLeft = topLeft,
            size = arcSz,
            style = Stroke(width = sw, cap = StrokeCap.Round)
        )

        if (progress <= 1f) {
            drawArc(
                color = foreground,
                startAngle = startAngle,
                sweepAngle = totalSweep * progress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSz,
                style = Stroke(width = sw, cap = StrokeCap.Round)
            )
        }
    }
}
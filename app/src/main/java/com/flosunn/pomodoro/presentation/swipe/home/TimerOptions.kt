package com.flosunn.pomodoro.presentation.swipe.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun TimerOptions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TimberOption(
            icon = R.drawable.ic_moon,
            title = "Focus",
        )

        TimberOption(
            icon = R.drawable.ic_mode,
            title = "Mode",
        )

        TimberOption(
            icon = R.drawable.ic_fullscreen,
            title = "Full Screen",
        )

        TimberOption(
            icon = R.drawable.ic_music,
            title = "Music",
        )
    }
}

@Composable
private fun RowScope.TimberOption(
    icon: Int,
    title: String,
    onPress: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .pointerInput(Unit) {
                detectTapGestures {
                    onPress()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color(0xFF676767),
        )

        Text(
            text = title,
            color = Color(0xFF676767),
            fontSize = 14.sp,
        )
    }
}
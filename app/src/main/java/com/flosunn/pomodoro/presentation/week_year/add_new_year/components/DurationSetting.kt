package com.flosunn.pomodoro.presentation.week_year.add_new_year.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.pomodoro.core.functions.TimeFuncs
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun DurationSetting(
    startTimeMilliseconds: Long, // Milliseconds since epoch
    endTimeMilliseconds: Long, // Milliseconds since epoch
    onSelectStartTime: () -> Unit,
    onSelectEndTime: () -> Unit,
) {
    val startTimeString = TimeFuncs.formatTimeString(startTimeMilliseconds)
    val endTimeString = TimeFuncs.formatTimeString(endTimeMilliseconds)

    Text(
        text = "Duration",
        fontSize = 18.sp,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .width(140.dp)
                .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                .border(
                    width = 1.dp,
                    color = Color(0xFFCCCCCC),
                    shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                )
                .rippleEffectClickable { onSelectStartTime() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = startTimeString,
                fontSize = 16.sp,
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .height(1.dp)
                .weight(1f),
            color = Color(0xFFCCCCCC)
        )

        Box(
            modifier = Modifier
                .height(48.dp)
                .width(140.dp)
                .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                .border(
                    width = 1.dp,
                    color = Color(0xFFCCCCCC),
                    shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                )
                .rippleEffectClickable { onSelectEndTime() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = endTimeString,
                fontSize = 16.sp,
            )
        }
    }
}
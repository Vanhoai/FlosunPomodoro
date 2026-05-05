package com.flosun.pomodoro.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.functions.TimeFuncs
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun TwelveWeekYearCard(
    cover: String,
    name: String,
    startTimeMilliseconds: Long,
    endTimeMilliseconds: Long,
    modifier: Modifier = Modifier,
    onPress: () -> Unit = {},
) {
    val startTime = TimeFuncs.formatTimeString(startTimeMilliseconds)
    val endTime = TimeFuncs.formatTimeString(endTimeMilliseconds)
    val duration = "$startTime - $endTime"

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium)),
        contentAlignment = Alignment.Center
    ) {
        CoreAsyncImage(
            url = cover,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000).copy(alpha = 0.3f))
                .padding(12.dp),
        ) {
            Text(
                text = name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = duration,
                fontSize = 16.sp,
                color = Color.White,
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    CoreButton(
                        modifier = Modifier.width(200.dp),
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFFFFF),
                                Color(0xFFFFFFFF),
                            )
                        ),
                        onPress = onPress,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Execution",
                                fontSize = 16.sp,
                                color = Color(0xFF141B34),
                            )

                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_next),
                                contentDescription = null,
                                tint = Color(0xFF141B34),
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(18.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.flosun.pomodoro.presentation.week_year.year_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.functions.TimeFuncs

@Composable
fun WeekProgressedCard(
    name: String,
    startTimeMilliseconds: Long,
    endTimeMilliseconds: Long,
    progress: Int = 0,
    isCompleted: Boolean = false,
    navigateToWeek: () -> Unit = {},
) {
    val duration = "${TimeFuncs.formatTimeString(startTimeMilliseconds)} - ${
        TimeFuncs.formatTimeString(endTimeMilliseconds)
    }"

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFDADADA),
                shape = RoundedCornerShape(8.dp)
            )
            .rippleEffectClickable { navigateToWeek() }
            .padding(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFCBDD62)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_flash_message),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = name,
                            fontSize = 18.sp,
                            color = Color.Black,
                        )

                        Text(
                            text = duration,
                            fontSize = 14.sp,
                            color = Color(0xFFA2A2A2),
                        )
                    }

                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            progress = { progress / 100f },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Transparent),
                            color = Color(0xFF3FA039),
                            trackColor = Color(0xFFE0E0E0),
                        )

                        Text(
                            text = "$progress%",
                            fontSize = 14.sp,
                            color = Color.Black,
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Self-Reward",
                        fontSize = 16.sp,
                        color = Color.Black,
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(20.dp)
                            .background(
                                color = if (isCompleted) Color(0xFF3FA039) else Color(0xFFD8D8D8),
                                shape = RoundedCornerShape(4.dp)
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_checked),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}
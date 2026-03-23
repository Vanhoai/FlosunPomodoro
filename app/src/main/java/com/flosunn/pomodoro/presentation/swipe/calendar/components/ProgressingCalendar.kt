package com.flosunn.pomodoro.presentation.swipe.calendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.swipe.calendar.days
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun ProgressingCalendar() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF414141)
            )

            Text(
                text = "December 2026",
                fontSize = 18.sp,
                color = Color(0xFF414141),
                textAlign = TextAlign.Center,
            )

            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF414141),
            )
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            days.forEach { day ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        fontSize = 14.sp,
                        color = Color(0xFF414141),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        repeat(4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                repeat(7) { index ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { (index + 1) / 7f },
                            color = AppTheme.colors.primaryColor,
                            trackColor = Color(0xFFE0E0E0),
                        )

                        Text(
                            text = "${index + 1}",
                            fontSize = 14.sp,
                            color = Color(0xFF414141),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
package com.flosunn.pomodoro.presentation.swipe.calendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun SummaryCalendar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(AppTheme.sizing.borderMedium),
            )
            .padding(vertical = 12.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Progress",
                color = Color(0xFF414141),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )

            Text(
                text = "71.4 %",
                color = Color(0xFF414141),
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Worked",
                color = Color(0xFF414141),
                fontSize = 14.sp,
            )

            Text(
                text = "300 mins",
                color = Color(0xFF414141),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Sessions",
                color = Color(0xFF414141),
                fontSize = 14.sp,
            )

            Text(
                text = "12",
                color = Color(0xFF414141),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
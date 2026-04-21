package com.flosunn.pomodoro.ui.components.shared

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.ui.components.core.CoreAsyncImage
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun TwelveWeekYearCard(
    modifier: Modifier = Modifier,
    onPress: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium)),
        contentAlignment = Alignment.Center
    ) {
        CoreAsyncImage(
            url = "https://i.pinimg.com/736x/89/1d/8c/891d8cd9e96d9d03c6aa185b83c046d0.jpg",
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000).copy(alpha = 0.3f))
                .padding(12.dp),
        ) {
            Text(
                text = "The Beginning",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

            Text(
                text = "12/23/2026 - 12/23/2026",
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
                                painter = painterResource(R.drawable.ic_arrow_right_2),
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
package com.flosun.pomodoro.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.presentation.notification.Notification
import com.flosun.pomodoro.ui.theme.AppTheme


@Composable
fun NotificationCard(
    notification: Notification,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = AppTheme.colors.borderColor,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(notification.icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF717171),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        ) {
            Text(
                text = notification.title,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = notification.description,
                fontSize = 14.sp,
                color = Color(0xFFAFAFAF),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                text = "09:00 AM",
                fontSize = 14.sp,
                color = Color(0xFFAFAFAF),
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!notification.isRead) Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = Color(0xFFFF5B5B),
                        shape = CircleShape,
                    )
            )

            Icon(
                painter = painterResource(R.drawable.ic_arrow_next_2),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color(0xFF898989),
            )
        }
    }
}
package com.flosun.pomodoro.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.components.shared.CommonBackHeading
import com.flosun.pomodoro.ui.components.shared.NamedDivider
import com.flosun.pomodoro.ui.components.shared.NotificationCard
import com.flosun.pomodoro.ui.theme.AppTheme
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Notification @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val title: String,
    val description: String,
    val time: Long,
    val icon: Int,
    val isRead: Boolean = false,
)

data class NotificationSection(
    val title: String,
    val notifications: List<Notification>,
)

val sections = listOf(
    NotificationSection(
        title = "Today",
        notifications = listOf(
            Notification(
                title = "New Login from a Device ! \uD83D\uDD10",
                description = "Your account was accessed from a Honor X6B in HCMC. Was this you ?",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
            Notification(
                title = "Unusual Activity Detected \uD83D\uDEA8",
                description = "We noticed some unusual activity on your account. Please review your recent activity to ensure your account's security.",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
        )
    ),
    NotificationSection(
        title = "Yesterday, Dec 24, 2003",
        notifications = listOf(
            Notification(
                title = "New Login from a Device ! \uD83D\uDD10",
                description = "Your account was accessed from a Honor X6B in HCMC. Was this you ?",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
            Notification(
                title = "Unusual Activity Detected \uD83D\uDEA8",
                description = "We noticed some unusual activity on your account. Please review your recent activity to ensure your account's security.",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
            Notification(
                title = "Unusual Activity Detected \uD83D\uDEA8",
                description = "We noticed some unusual activity on your account. Please review your recent activity to ensure your account's security.",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
            Notification(
                title = "Unusual Activity Detected \uD83D\uDEA8",
                description = "We noticed some unusual activity on your account. Please review your recent activity to ensure your account's security.",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
        )
    ),
    NotificationSection(
        title = "Yesterday, Dec 24, 2003",
        notifications = listOf(
            Notification(
                title = "New Login from a Device ! \uD83D\uDD10",
                description = "Your account was accessed from a Honor X6B in HCMC. Was this you ?",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
            Notification(
                title = "Unusual Activity Detected \uD83D\uDEA8",
                description = "We noticed some unusual activity on your account. Please review your recent activity to ensure your account's security.",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
            Notification(
                title = "Unusual Activity Detected \uD83D\uDEA8",
                description = "We noticed some unusual activity on your account. Please review your recent activity to ensure your account's security.",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
            Notification(
                title = "Unusual Activity Detected \uD83D\uDEA8",
                description = "We noticed some unusual activity on your account. Please review your recent activity to ensure your account's security.",
                time = System.currentTimeMillis(),
                icon = R.drawable.ic_box,
            ),
        )
    ),
)

@Composable
fun NotificationView() {
    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            CommonBackHeading(
                title = "Notifications",
                actions = {},
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                sections.forEach { section ->
                    stickyHeader {
                        NamedDivider(
                            name = section.title,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .background(Color.White)
                                .padding(bottom = 12.dp)
                        )
                    }

                    items(
                        items = section.notifications,
                        key = { notification -> notification.id },
                    ) { notification ->
                        NotificationCard(notification)
                    }
                }

                item { VerticalDivider(modifier = Modifier.height(60.dp)) }
            }
        }
    }
}

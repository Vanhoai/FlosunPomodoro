package com.flosun.pomodoro.ui.components.shared

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.LocalGlobalStore
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.CURRENT_ACCOUNT_KEY
import com.flosun.pomodoro.presentation.graph.NavGraph
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.rememberNavBackStack
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import com.flosun.pomodoro.ui.theme.AppTheme
import com.flosun.pomodoro.ui.theme.PomodoroTheme
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.extensions.tapGesture
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@Composable
fun SharedSwipeHeading(onPressAvatar: () -> Unit = {}) {
    val globalStore = LocalGlobalStore.current
    val navBackStack = rememberNavBackStack()
    val currentAccount by globalStore.account.collectAsState()
    if (currentAccount == null) return

    fun sayWelcome(): String {
        val time = System.currentTimeMillis()
        val datetime = Instant
            .fromEpochMilliseconds(time)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val hour = datetime.hour
        return when (hour) {
            in 0..10 -> "Good Morning 🌤️"
            in 10..12 -> "Good Afternoon ☀️"
            in 12..18 -> "Good Afternoon ☀️"
            else -> "Good Night ✨"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CoreAsyncImage(
            url = currentAccount!!.avatar,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            onPress = { onPressAvatar() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Hi, ${currentAccount!!.name}",
                fontSize = 18.sp,
            )

            Text(
                text = sayWelcome(),
                fontSize = 16.sp,
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_notification),
            contentDescription = null,
            tint = Color(0xFF797979),
            modifier = Modifier
                .size(28.dp)
                .tapGesture { navBackStack.add(NavRoute.Notifications) },
        )
    }
}

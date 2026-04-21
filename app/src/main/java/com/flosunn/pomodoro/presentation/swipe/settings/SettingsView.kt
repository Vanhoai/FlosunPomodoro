package com.flosunn.pomodoro.presentation.swipe.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.R
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.shared.LocalGlobalLoading
import com.flosunn.pomodoro.ui.components.shared.SharedSwipeHeading
import com.flosunn.pomodoro.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SettingItem(
    val icon: Int,
    val title: String,
    val route: NavRoute? = null
)

data class GroupedSetting(
    val title: String,
    val items: List<SettingItem>,
)

val settings = listOf(
    GroupedSetting(
        title = "General",
        items = listOf(
            SettingItem(
                icon = R.drawable.ic_tie,
                title = "12 Week Year",
                route = NavRoute.TwelveWeekYear
            ),
            SettingItem(
                icon = R.drawable.ic_box,
                title = "Preferences",
                route = NavRoute.Preference
            ),
            SettingItem(
                icon = R.drawable.ic_notification,
                title = "Notification",
                route = NavRoute.NotificationSettings
            ),
            SettingItem(
                icon = R.drawable.ic_language,
                title = "Appearance",
                route = NavRoute.Appearance
            ),
        )
    ),
    GroupedSetting(
        title = "Security",
        items = listOf(
            SettingItem(
                icon = R.drawable.ic_biometric,
                title = "Authentication",
                route = NavRoute.BiometricAuthentication,
            ),
            SettingItem(icon = R.drawable.ic_security, title = "Term & Privacy"),
        )
    ),
    GroupedSetting(
        title = "Developer Options",
        items = listOf(
            SettingItem(icon = R.drawable.ic_terminal, title = "Developer Options"),
        )
    )
)

@Composable
fun SettingsView(navBackStack: NavBackStack<NavKey>) {
    val scope = rememberCoroutineScope()
    val globalLoading = LocalGlobalLoading.current

    val signOutFunc = remember {
        {
            scope.launch {
                globalLoading.setLoading(true, "Signing Out")
                Firebase.auth.signOut()
                navBackStack.clear()
                globalLoading.setLoading(false)
                navBackStack.add(NavRoute.Auth)
            }
        }
    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        item {
            SharedSwipeHeading(onPressAvatar = { navBackStack.add(NavRoute.Account) })
        }

        for (group in settings) {
            item {
                Text(
                    text = group.title,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 12.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                        .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                        ),
                ) {
                    for (i in group.items.indices) {
                        val item = group.items[i]

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(),
                                    onClick = {
                                        item.route?.let { navBackStack.add(it) }
                                    }
                                )
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color(0xFF636363)
                            )

                            Text(
                                text = item.title,
                                fontSize = 16.sp,
                                color = Color(0xFF636363),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 12.dp)
                            )

                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_right),
                                contentDescription = null,
                                tint = Color(0xFF636363),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        if (i != group.items.size - 1) HorizontalDivider(
                            color = Color(0xFFE1E1E1),
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFECECEC),
                        shape = RoundedCornerShape(AppTheme.sizing.borderMedium)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = { signOutFunc() }
                    )
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_logout),
                    contentDescription = null,
                    tint = Color(0xFFFF6767),
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    color = Color(0xFFFF6767),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
package com.flosunn.pomodoro.presentation.swipe.settings.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.core.constants.DurationType
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.components.shared.RowNavigation
import com.flosunn.pomodoro.ui.components.shared.RowSwitch
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun PreferenceView(navBackStack: NavBackStack<NavKey>) {
    var isDisableBreak by remember { mutableStateOf(false) }
    var isAutoStartNextPomodoro by remember { mutableStateOf(false) }
    var isAutoStartBreak by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = AppTheme.colors.backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            CommonBackHeading(
                onBack = { navBackStack.removeLastOrNull() },
                title = "Preferences"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White),
            ) {
                RowNavigation(
                    title = "Pomodoro Duration",
                    description = "30 minutes",
                    onPress = {
                        navBackStack.add(NavRoute.ChooseDuration(DurationType.POMODORO))
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowNavigation(
                    title = "Short Break Duration",
                    description = "5 minutes",
                    onPress = {
                        navBackStack.add(NavRoute.ChooseDuration(DurationType.SHORT_BREAK))
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowNavigation(
                    title = "Long Break Duration",
                    description = "15 minutes",
                    onPress = {
                        navBackStack.add(NavRoute.ChooseDuration(DurationType.LONG_BREAK))
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowNavigation(
                    title = "Long Break After",
                    description = "4 Pomodoros",
                    onPress = {
                        navBackStack.add(NavRoute.ChooseDuration(DurationType.LONG_BREAK_AFTER))
                    }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowSwitch(
                    title = "Disable Break",
                    isActive = isDisableBreak,
                    onChange = { isDisableBreak = it }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowSwitch(
                    title = "Auto Start Next Pomodoro",
                    isActive = isAutoStartNextPomodoro,
                    onChange = { isAutoStartNextPomodoro = it }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowSwitch(
                    title = "Auto Start Break",
                    isActive = isAutoStartBreak,
                    onChange = { isAutoStartBreak = it }
                )
            }
        }
    }
}
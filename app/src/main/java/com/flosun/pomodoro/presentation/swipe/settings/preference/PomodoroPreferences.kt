package com.flosun.pomodoro.presentation.swipe.settings.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.core.constants.DurationType
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.shared.RowNavigation
import com.flosun.pomodoro.ui.components.shared.RowSwitch
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun PomodoroPreferences(
    pomodoroDuration: Int = 30,
    shortBreakDuration: Int = 5,
    longBreakDuration: Int = 15,
    longBreakAfter: Int = 4,
    isDisabledBreak: Boolean,
    isAutoStartNextPomodoro: Boolean,
    isAutoStartBreak: Boolean,
    onChangedIsDisabledBreak: (Boolean) -> Unit,
    onChangedIsAutoStartNextPomodoro: (Boolean) -> Unit,
    onChangedIsAutoStartBreak: (Boolean) -> Unit,
) {
    val navBackStack = LocalNavBackStack.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
            .background(Color.White),
    ) {
        RowNavigation(
            title = "Pomodoro Duration",
            description = "$pomodoroDuration Minutes",
            onPress = { navBackStack.add(NavRoute.ChooseDuration(DurationType.POMODORO_DURATION)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Short Break Duration",
            description = "$shortBreakDuration Minutes",
            onPress = { navBackStack.add(NavRoute.ChooseDuration(DurationType.SHORT_BREAK_DURATION)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Long Break Duration",
            description = "$longBreakDuration Minutes",
            onPress = { navBackStack.add(NavRoute.ChooseDuration(DurationType.LONG_BREAK_DURATION)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Long Break After",
            description = "$longBreakAfter Pomodoros",
            onPress = { navBackStack.add(NavRoute.ChooseDuration(DurationType.LONG_BREAK_AFTER)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowSwitch(
            title = "Disable Break",
            isActive = isDisabledBreak,
            onChange = onChangedIsDisabledBreak,
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowSwitch(
            title = "Auto Start Next Pomodoro",
            isActive = isAutoStartNextPomodoro,
            onChange = onChangedIsAutoStartNextPomodoro
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowSwitch(
            title = "Auto Start Break",
            isActive = isAutoStartBreak,
            onChange = onChangedIsAutoStartBreak
        )
    }
}
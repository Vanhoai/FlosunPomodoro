package com.flosun.pomodoro.presentation.swipe.settings.preference

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.adapters.database.entities.SoundEntity
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.swipe.settings.preference.choose_sound.ChooseSoundType
import com.flosun.pomodoro.ui.components.shared.RowNavigation
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun AlarmPreferences(
    focusAlarm: SoundEntity,
    shortBreakAlarm: SoundEntity,
    longBreakAlarm: SoundEntity,
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
            title = "Focus Alarm",
            description = focusAlarm.title,
            onPress = { navBackStack.add(NavRoute.ChooseSound(ChooseSoundType.FOCUS_ALARM)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Short Break Alarm",
            description = shortBreakAlarm.title,
            onPress = { navBackStack.add(NavRoute.ChooseSound(ChooseSoundType.SHORT_BREAK_ALARM)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Long Break Alarm",
            description = longBreakAlarm.title,
            onPress = { navBackStack.add(NavRoute.ChooseSound(ChooseSoundType.LONG_BREAK_ALARM)) }
        )
    }
}
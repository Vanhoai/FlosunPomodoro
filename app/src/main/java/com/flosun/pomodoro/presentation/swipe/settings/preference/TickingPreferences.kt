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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.adapters.database.entities.SoundEntity
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.presentation.swipe.settings.preference.choose_sound.ChooseSoundType
import com.flosun.pomodoro.ui.components.shared.RowNavigation
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun TickingPreferences(
    focusTicking: SoundEntity,
    shortBreakTicking: SoundEntity,
    longBreakTicking: SoundEntity,
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
            title = "Focus Ticking",
            description = focusTicking.title,
            onPress = { navBackStack.add(NavRoute.ChooseSound(ChooseSoundType.FOCUS_TICKING)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Short Break Ticking",
            description = shortBreakTicking.title,
            onPress = { navBackStack.add(NavRoute.ChooseSound(ChooseSoundType.SHORT_BREAK_TICKING)) }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Long Break Ticking",
            description = longBreakTicking.title,
            onPress = { navBackStack.add(NavRoute.ChooseSound(ChooseSoundType.LONG_BREAK_TICKING)) }
        )
    }
}
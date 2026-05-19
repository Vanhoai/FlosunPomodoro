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
import com.flosun.pomodoro.LocalNavBackStack
import com.flosun.pomodoro.core.constants.DurationType
import com.flosun.pomodoro.presentation.graph.NavRoute
import com.flosun.pomodoro.ui.components.shared.RowNavigation
import com.flosun.pomodoro.ui.components.shared.RowSwitch
import com.flosun.pomodoro.ui.theme.AppTheme

@Composable
fun DailyTargetPreferences(
    dailyTarget: Int = 420,
    isEnabledDailyTarget: Boolean = false,
    onChangedIsEnabledDailyTarget: (Boolean) -> Unit = {},
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
        RowSwitch(
            title = "Daily Targets",
            isActive = isEnabledDailyTarget,
            onChange = onChangedIsEnabledDailyTarget,
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Daily Target Minutes",
            description = "$dailyTarget Minutes",
            onPress = { navBackStack.add(NavRoute.ChooseDuration(DurationType.DAILY_TARGET_DURATION)) }
        )
    }
}
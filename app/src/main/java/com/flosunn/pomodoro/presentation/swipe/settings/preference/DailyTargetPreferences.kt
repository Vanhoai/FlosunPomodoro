package com.flosunn.pomodoro.presentation.swipe.settings.preference

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
import com.flosunn.pomodoro.core.constants.DurationType
import com.flosunn.pomodoro.presentation.graph.NavRoute
import com.flosunn.pomodoro.ui.components.shared.RowNavigation
import com.flosunn.pomodoro.ui.components.shared.RowSwitch
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun DailyTargetPreferences(navBackStack: NavBackStack<NavKey>) {
    var isEnabledDailyTarget by remember { mutableStateOf(false) }

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
            onChange = { isEnabledDailyTarget = it }
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Daily Target Minutes",
            description = "420 minutes",
            onPress = {}
        )
    }
}
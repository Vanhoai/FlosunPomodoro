package com.flosunn.pomodoro.presentation.swipe.settings.preference

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
import com.flosunn.pomodoro.ui.components.shared.RowNavigation
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun AlarmPreferences(navBackStack: NavBackStack<NavKey>) {
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
            description = "Deep Focus",
            onPress = {}
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Short Break Alarm",
            description = "Steady Beat",
            onPress = {}
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFEDEDED),
        )

        RowNavigation(
            title = "Long Break Alarm",
            description = "Deep Focus",
            onPress = {}
        )
    }
}
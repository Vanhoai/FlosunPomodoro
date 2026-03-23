package com.flosunn.pomodoro.presentation.swipe.settings.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.ui.components.shared.CommonBackHeading
import com.flosunn.pomodoro.ui.components.shared.RowNavigation
import com.flosunn.pomodoro.ui.theme.AppTheme

@Composable
fun AppearanceView(navBackStack: NavBackStack<NavKey>) {
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
                title = "App Appearance"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(AppTheme.sizing.borderMedium))
                    .background(Color.White),
            ) {
                RowNavigation(
                    title = "Theme",
                    description = "Light",
                    onPress = { }
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFEDEDED),
                )

                RowNavigation(
                    title = "App Language",
                    description = "English",
                    onPress = {}
                )
            }
        }
    }
}
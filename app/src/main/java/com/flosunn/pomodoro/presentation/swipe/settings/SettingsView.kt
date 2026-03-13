package com.flosunn.pomodoro.presentation.swipe.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.flosunn.pomodoro.ui.components.core.CoreButton
import com.flosunn.pomodoro.ui.theme.AppTheme


@Composable
fun SettingsView(navBackStack: NavBackStack<NavKey>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CoreButton(
            modifier = Modifier.padding(horizontal = 20.dp),
            onPress = {
                navBackStack.removeLastOrNull()
            },
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.White,
                    Color.White
                )
            ),
        ) {
            Text(
                text = "Logout",
                style = AppTheme.typography.body,
                color = AppTheme.colors.primaryColor
            )
        }
    }
}